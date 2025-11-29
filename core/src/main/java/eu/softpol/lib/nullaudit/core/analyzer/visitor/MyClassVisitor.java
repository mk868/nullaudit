package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;

import eu.softpol.lib.nullaudit.core.model.ImmutableNAClass;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAComponent;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAField;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAMethod;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAClass;
import eu.softpol.lib.nullaudit.core.signature.FieldSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.MethodSignatureAnalyzer;
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

  private final List<ClassReference> classChain = new ArrayList<>();
  private final ImmutableNAClass.Builder naClassBuilder = ImmutableNAClass.builder();
  private @Nullable String sourceFileName;
  private @Nullable ClassReference thisClass;
  private @Nullable ClassReference outerClass;
  private @Nullable NAClass naClass;

  public MyClassVisitor() {
    super(Opcodes.ASM9);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    this.thisClass = ClassReference.of(name);
    naClassBuilder.thisClazz(this.thisClass)
        .superClazz(ClassReference.of(superName));
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
    var naComponentBuilder = ImmutableNAComponent.builder()
        .componentName(name)
        .componentDescriptor(descriptor)
        .componentSignature(signature)
        .type(fs);

    return new MyRecordComponentVisitor(naComponentBuilder, fs, () -> {
      naClassBuilder.addComponents(naComponentBuilder.build());
    });
  }

  @Override
  public FieldVisitor visitField(int access, String name, String descriptor,
      @Nullable String signature, Object value) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitField(access, name, descriptor, signature, value);
    }

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var naFieldBuilder = ImmutableNAField.builder()
        .fieldName(name)
        .fieldDescriptor(descriptor)
        .fieldSignature(signature)
        .type(fs);

    return new MyFieldVisitor(naFieldBuilder, fs, () -> {
      naClassBuilder.addFields(naFieldBuilder.build());
    });
  }

  @Override
  public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor,
      @Nullable String methodSignature, String[] exceptions) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitMethod(access, methodName, methodDescriptor, methodSignature, exceptions);
    }

    MethodSignature ms = analyzeMethodSignature(methodName, methodDescriptor, methodSignature);

    String descriptiveMethodName = computeDescriptiveMethodName(methodName, methodDescriptor);

    var parameterTypes = ms.parameterTypes();

    var methodBuilder = ImmutableNAMethod.builder()
        .methodName(methodName)
        .descriptiveMethodName(descriptiveMethodName)
        .methodDescriptor(methodDescriptor)
        .methodSignature(methodSignature)
        .returnType(ms.returnType());

    return new MyMethodVisitor(methodBuilder, ms.returnType(), parameterTypes, () -> {
      naClassBuilder.addMethods(methodBuilder.build());
    });
  }

  private static MethodSignature analyzeMethodSignature(String methodName, String methodDescriptor,
      @Nullable String methodSignature) {
    final MethodSignature ms;
    try {
      ms = MethodSignatureAnalyzer.analyze(requireNonNullElse(methodSignature, methodDescriptor));
    } catch (RuntimeException e) {
      throw new RuntimeException(
          "Reading signature of " + methodName + methodDescriptor + " (" + methodSignature
          + ") failed", e);
    }
    return ms;
  }

  private String computeDescriptiveMethodName(String methodName, String methodDescriptor) {
    String simpleName;
    if (methodName.equals("<init>")) {
      simpleName = thisClass.simpleName();
    } else {
      simpleName = methodName;
    }
    return simpleName + Arrays.stream(Type.getArgumentTypes(methodDescriptor))
        .map(Type::getClassName)
        .collect(Collectors.joining(", ", "(", ")"));
  }

  @Override
  public void visitEnd() {
    determineTopClass();
    naClass = naClassBuilder.build();
    super.visitEnd();
  }

  private void determineTopClass() {
    if (classChain.isEmpty()) {
      if (outerClass != null) {
        naClassBuilder.topClass(outerClass);
      } else {
        naClassBuilder.topClass(thisClass);
      }
    } else {
      naClassBuilder.topClass(classChain.get(0));
    }
  }

  public NAClass getNaClass() {
    return Objects.requireNonNull(naClass, "naClass");
  }

}
