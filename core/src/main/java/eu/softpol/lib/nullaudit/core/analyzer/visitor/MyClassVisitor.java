package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAClass;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAMethod;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAClass;
import eu.softpol.lib.nullaudit.core.model.NAComponent;
import eu.softpol.lib.nullaudit.core.model.NAField;
import eu.softpol.lib.nullaudit.core.signature.FieldSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.MethodSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;

public class MyClassVisitor extends org.objectweb.asm.ClassVisitor {

  private static final System.Logger logger = System.getLogger(MyClassVisitor.class.getName());

  private final AnalysisContext context;
  private final List<ClassReference> classChain = new ArrayList<>();
  private final ImmutableNAClass.Builder naClassBuilder = ImmutableNAClass.builder();
  private final List<ImmutableNAMethod.Builder> methodBuilders = new ArrayList<>();
  private @Nullable String sourceFileName;
  private @Nullable ClassReference thisClass;
  private @Nullable ClassReference outerClass;
  private @Nullable NAClass naClass;

  public MyClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    this.thisClass = ClassReference.of(name);
    naClassBuilder.thisClazz(this.thisClass)
        .superClazz(ClassReference.of(superName))
        .effectiveNullScope(NullScope.NOT_DEFINED);
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitOuterClass(String owner, String name, String descriptor) {
    outerClass = ClassReference.of(owner);
    naClassBuilder.outerClass(outerClass);
    super.visitOuterClass(owner, name, descriptor);
  }

  @Override
  public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName,
      int access) {
    if (outerName != null && thisClass.internalName().startsWith(name)) {
      classChain.add(ClassReference.of(outerName));
    }
    if (name.equals(thisClass.internalName()) && outerName != null) {
      outerClass = ClassReference.of(outerName);
      naClassBuilder.outerClass(outerClass);
    }
    super.visitInnerClass(name, outerName, innerName, access);
  }

  @Override
  public void visitSource(String source, @Nullable String debug) {
    sourceFileName = source;
    super.visitSource(source, debug);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naClassBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
      @Nullable String signature) {

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var naComponent = new NAComponent(name, descriptor, signature, fs);
    naClassBuilder.addComponents(naComponent);

    return new MyRecordComponentVisitor(naComponent);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String descriptor,
      @Nullable String signature, Object value) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitField(access, name, descriptor, signature, value);
    }

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var naField = new NAField(name, descriptor, signature, fs);
    naClassBuilder.addFields(naField);

    return new MyFieldVisitor(naField);
  }

  @Override
  public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor,
      @Nullable String methodSignature, String[] exceptions) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitMethod(access, methodName, methodDescriptor, methodSignature, exceptions);
    }

    final MethodSignature ms;
    try {
      ms = MethodSignatureAnalyzer.analyze(requireNonNullElse(methodSignature, methodDescriptor));
    } catch (RuntimeException e) {
      throw new RuntimeException(
          "Reading signature of " + methodName + methodDescriptor + " (" + methodSignature
          + ") failed", e);
    }
    var parameterTypes = ms.parameterTypes();

    if (!classChain.isEmpty()) {
      // it's probably inner class
      var outerClassName = classChain.get(classChain.size() - 1).name();
      // check if the first constructor's argument has outer class type
      if (methodName.equals("<init>") &&
          !parameterTypes.isEmpty() &&
          parameterTypes.get(0) instanceof ClassTypeNode classTypeNode &&
          classTypeNode.getClazz().equals(outerClassName)) {
        classTypeNode.addAnnotation(TypeUseAnnotation.JSPECIFY_NON_NULL); // TODO should not be here
      }
      // alternatively, I can search for the ACC_SYNTHETIC field of outerClassName type, but the compiler can skip this field when it's not used.
    }

    String descriptiveMethodName;
    if (methodName.equals("<init>")) {
      descriptiveMethodName = thisClass.simpleName();
    } else {
      descriptiveMethodName = methodName;
    }
    descriptiveMethodName += Arrays.stream(Type.getArgumentTypes(methodDescriptor))
        .map(Type::getClassName)
        .collect(Collectors.joining(", ", "(", ")"));

    var methodBuilder = ImmutableNAMethod.builder()
        .methodName(methodName)
        .descriptiveMethodName(descriptiveMethodName)
        .methodDescriptor(methodDescriptor)
        .methodSignature(methodSignature)
        .effectiveNullScope(NullScope.NOT_DEFINED)
        .ms(ms);
    methodBuilders.add(methodBuilder);

    return new MyMethodVisitor(methodBuilder, ms);
  }

  @Override
  public void visitEnd() {
    if (classChain.isEmpty()) {
      if (outerClass != null) {
        naClassBuilder.topClass(outerClass);
      } else {
        naClassBuilder.topClass(thisClass);
      }
    } else {
      naClassBuilder.topClass(classChain.get(0));
    }
    // Build a temporary class with NOT_DEFINED to extract annotations
    var tempClass = naClassBuilder
        .effectiveNullScope(NullScope.NOT_DEFINED)
        .build();
    context.setClassNullScope(thisClass,
        NullScope.from(tempClass.annotations()));
    var nullScopes = getNullScopesForClass();

    var classEffective = getEffectiveNullMarkedScope(nullScopes);
    // Set the real effective scope now
    naClassBuilder.effectiveNullScope(classEffective);

    // Build methods, compute their effective scopes, and add to class builder
    for (var mb : methodBuilders) {
      var built = mb.build();
      var methodEffective = getEffectiveNullMarkedScope(
          List.of(classEffective, NullScope.from(built.annotations())));
      naClassBuilder.addMethods(built.withEffectiveNullScope(methodEffective));
    }

    // Build final class
    naClass = naClassBuilder.build();

    super.visitEnd();
  }

  public NAClass getNaClass() {
    return Objects.requireNonNull(naClass, "naClass");
  }

  private List<NullScope> getNullScopesForClass() {
    var nullScopes = new ArrayList<NullScope>();
    if (context.isModuleInfoNullMarked()) {
      nullScopes.add(NullScope.NULL_MARKED);
    }
    nullScopes.add(context.getPackageNullScope(thisClass.packageName()));
    for (var outerClass : classChain) {
      nullScopes.add(context.getClassNullScope(outerClass));
    }
    nullScopes.add(context.getClassNullScope(thisClass));
    return List.copyOf(nullScopes);
  }

  /**
   * @param nullScopes null scopes from inner to outer
   * @return effective nullness scope
   */
  private static NullScope getEffectiveNullMarkedScope(List<NullScope> nullScopes) {
    return nullScopes.stream()
        .filter(not(isEqual(NullScope.NOT_DEFINED)))
        .reduce((f, s) -> s) // get last
        .orElse(NullScope.NULL_UNMARKED);
  }

}
