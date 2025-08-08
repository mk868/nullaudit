package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.MutableNAClass;
import eu.softpol.lib.nullaudit.core.model.MutableNAMethod;
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
  private String sourceFileName;
  private MutableNAClass naClass;

  public MyClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    naClass = new MutableNAClass(
        ClassReference.of(name),
        ClassReference.of(superName)
    );
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitOuterClass(String owner, String name, String descriptor) {
    naClass.setOuterClass(ClassReference.of(owner));
    super.visitOuterClass(owner, name, descriptor);
  }

  @Override
  public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName,
      int access) {
    if (outerName != null && naClass.thisClazz().internalName().startsWith(name)) {
      classChain.add(ClassReference.of(outerName));
    }
    if (name.equals(naClass.thisClazz().internalName()) && outerName != null) {
      naClass.setOuterClass(ClassReference.of(outerName));
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
    naClass.addAnnotation(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
      @Nullable String signature) {

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var naComponent = new NAComponent(name, descriptor, signature, fs);
    naClass.addComponent(naComponent);

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
    naClass.addField(naField);

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
      descriptiveMethodName = naClass.thisClazz().simpleName();
    } else {
      descriptiveMethodName = methodName;
    }
    descriptiveMethodName += Arrays.stream(Type.getArgumentTypes(methodDescriptor))
        .map(Type::getClassName)
        .collect(Collectors.joining(", ", "(", ")"));

    var methodInfo = new MutableNAMethod(methodName, descriptiveMethodName, methodDescriptor,
        methodSignature, ms);
    naClass.addMethod(methodInfo);

    return new MyMethodVisitor(methodInfo);
  }

  @Override
  public void visitEnd() {
    if (classChain.isEmpty()) {
      if (naClass.outerClass() != null) {
        naClass.setTopClazz(naClass.outerClass());
      } else {
        naClass.setTopClazz(naClass.thisClazz());
      }
    } else {
      naClass.setTopClazz(classChain.get(0));
    }
    context.setClassNullScope(naClass.thisClazz(),
        NullScope.from(naClass.annotations()));
    var nullScopes = getNullScopesForClass();

    naClass.setEffectiveNullScope(getEffectiveNullMarkedScope(nullScopes));

    naClass.methods().stream()
        .map(MutableNAMethod.class::cast)
        .forEach(vm -> vm.setEffectiveNullScope(getEffectiveNullMarkedScope(
            List.of(naClass.effectiveNullScope(), NullScope.from(vm.annotations())))));

    super.visitEnd();
  }

  public NAClass getNaClass() {
    return naClass;
  }

  private List<NullScope> getNullScopesForClass() {
    var nullScopes = new ArrayList<NullScope>();
    if (context.isModuleInfoNullMarked()) {
      nullScopes.add(NullScope.NULL_MARKED);
    }
    nullScopes.add(context.getPackageNullScope(naClass.thisClazz().packageName()));
    for (var outerClass : classChain) {
      nullScopes.add(context.getClassNullScope(outerClass));
    }
    nullScopes.add(context.getClassNullScope(naClass.thisClazz()));
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
