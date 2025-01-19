package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.SignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  private final ReportBuilder reportBuilder;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private final List<ComponentInfo> components = new ArrayList<>();
  private final List<FieldInfo> fields = new ArrayList<>();
  private final List<MethodInfo> methods = new ArrayList<>();
  private Clazz superClazz;
  private final List<Clazz> outerClasses = new ArrayList<>();
  private Clazz thisClazz;
  private String sourceFileName;

  public MyClassVisitor(AnalysisContext context, ReportBuilder reportBuilder) {
    super(Opcodes.ASM9);
    this.context = context;
    this.reportBuilder = reportBuilder;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    thisClazz = Clazz.of(name);
    superClazz = Clazz.of(superName);
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName, int access) {
    if (outerName != null && thisClazz.internalName().startsWith(name)) {
      outerClasses.add(Clazz.of(outerName));
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
    if (descriptor.contains(Descriptors.NULL_MARKED)) {
      annotations.add(NullScopeAnnotation.NULL_MARKED);
    }
    if (descriptor.contains(Descriptors.NULL_UNMARKED)) {
      annotations.add(NullScopeAnnotation.NULL_UNMARKED);
    }
    if (descriptor.contains(Descriptors.KOTLIN_METADATA)) {
      annotations.add(NullScopeAnnotation.KOTLIN_METADATA);
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
      @Nullable String signature) {

    var fs = SignatureAnalyzer.analyzeFieldSignature(requireNonNullElse(signature, descriptor));
    var componentInfo = new ComponentInfo(name, descriptor, signature, fs);
    components.add(componentInfo);

    return new MyRecordComponentVisitor(fs);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String descriptor,
      @Nullable String signature, Object value) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitField(access, name, descriptor, signature, value);
    }

    var fs = SignatureAnalyzer.analyzeFieldSignature(requireNonNullElse(signature, descriptor));
    var fieldInfo = new FieldInfo(name, descriptor, signature, fs);
    fields.add(fieldInfo);

    return new MyFieldVisitor(fs);
  }

  @Override
  public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor,
      @Nullable String methodSignature, String[] exceptions) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitMethod(access, methodName, methodDescriptor, methodSignature, exceptions);
    }

    final MethodSignature ms;
    try {
      ms = SignatureAnalyzer.analyzeMethodSignature(
          requireNonNullElse(methodSignature, methodDescriptor));
    } catch (RuntimeException e) {
      throw new RuntimeException(
          "Reading signature of " + methodName + methodDescriptor + " (" + methodSignature
          + ") failed", e);
    }
    var parameterTypes = ms.parameterTypes();

    if (!outerClasses.isEmpty()) {
      // it's probably inner class
      var outerClassName = outerClasses.get(outerClasses.size() - 1).name();
      // check if the first constructor's argument has outer class type
      if (methodName.equals("<init>") &&
          !parameterTypes.isEmpty() &&
          parameterTypes.get(0).getValue().equals(outerClassName)) {
        parameterTypes.get(0).setOperator(NullnessOperator.MINUS_NULL);
      }
      // alternatively, I can search for the ACC_SYNTHETIC field of outerClassName type, but the compiler can skip this field when it's not used.
    }

    String descriptiveMethodName;
    if (methodName.equals("<init>")) {
      descriptiveMethodName = thisClazz.simpleName();
    } else {
      descriptiveMethodName = methodName;
    }
    descriptiveMethodName += "(" +
                             Arrays.stream(Type.getArgumentTypes(methodDescriptor))
                                 .map(Type::getClassName)
                                 .collect(Collectors.joining(", "))
                             + ")";

    var methodInfo = new MethodInfo(methodName, descriptiveMethodName, methodDescriptor,
        methodSignature, ms);
    methods.add(methodInfo);

    return new MyMethodVisitor(methodInfo);
  }

  @Override
  public void visitEnd() {
    context.setClassNullScope(thisClazz.name(), NullScope.from(annotations));

    reportBuilder.incSummaryTotalClasses();
    boolean unspecifiedNullnessFound = false;

    for (var componentInfo : components) {
      reportBuilder.incSummaryTotalFields();

      var nullScopes = getNullScopesForClass();
      var effectiveNullMarkedScope = getEffectiveNullMarkedScope(nullScopes);

      if (effectiveNullMarkedScope != NullScope.NULL_MARKED) {
        var s = "%s %s".formatted(
            componentInfo.fs().toString(),
            componentInfo.componentName()
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessFields();
          appendIssue(
              componentInfo.componentName(),
              """
                  Unspecified nullness found:
                  %s
                  %s
                  """.formatted(
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }

    for (var fieldInfo : fields) {
      if (superClazz.name().equals("java.lang.Record")) {
        // generated by compiler - ignore
        continue;
      }
      reportBuilder.incSummaryTotalFields();

      var nullScopes = getNullScopesForClass();
      var effectiveNullMarkedScope = getEffectiveNullMarkedScope(nullScopes);

      if (effectiveNullMarkedScope != NullScope.NULL_MARKED) {
        var s = "%s %s".formatted(
            fieldInfo.fs().toString(),
            fieldInfo.fieldName()
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessFields();
          appendIssue(
              fieldInfo.fieldName(),
              """
                  Unspecified nullness found:
                  %s
                  %s
                  """.formatted(
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }

    for (var methodInfo : methods) {
      reportBuilder.incSummaryTotalMethods();
      if (superClazz.name().equals("java.lang.Record")) {
        var methodName = methodInfo.methodName();
        if (methodName.equals("equals")) {
          continue;
        }
        if (methodName.equals("toString")) {
          continue;
        }
        if (components.stream()
                .filter(c -> c.componentName().equals(methodName))
                .anyMatch(c -> c.fs().toString().equals(methodInfo.ms().returnType().toString()))
            && methodInfo.ms().parameterTypes().isEmpty()
        ) {
          // skip default getter
          continue;
        }
        if (methodName.equals("<init>") && methodInfo.ms().parameterTypes().values()
            .stream()
            .map(TypeNode::toString)
            .collect(Collectors.joining(",")).equals(components.stream()
                .map(ComponentInfo::fs)
                .map(TypeNode::toString)
                .collect(Collectors.joining(",")))
        ) {
          // skip default constructor
          continue;
        }
      }

      var nullScopes = getNullScopesForClass();
      nullScopes.add(NullScope.from(methodInfo.annotations()));
      var effectiveNullMarkedScope = getEffectiveNullMarkedScope(nullScopes);

      if (effectiveNullMarkedScope != NullScope.NULL_MARKED) {
        var s = "%s %s(%s)".formatted(
            methodInfo.ms().returnType(),
            methodInfo.methodName(),
            methodInfo.ms()
                .parameterTypes().values().stream()
                .map(TypeNode::toString)
                .collect(Collectors.joining(", "))
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessMethods();
          appendIssue(
              methodInfo.descriptiveMethodName(),
              """
                  Unspecified nullness found:
                  %s
                  %s
                  """.formatted(
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }

    if (unspecifiedNullnessFound) {
      reportBuilder.incSummaryUnspecifiedNullnessClasses();
    }

    super.visitEnd();
  }

  private void appendIssue(String descriptiveMethodName, String message) {
    var location = "";
    if (context.getModuleName() != null) {
      location = context.getModuleName() + "/";
    }
    location += thisClazz.name();
    location += "#" + descriptiveMethodName;

    reportBuilder.addIssue(new Issue(
        location,
        List.of(Kind.UNSPECIFIED_NULLNESS),
        message
    ));
  }

  private List<NullScope> getNullScopesForClass() {
    var nullScopes = new ArrayList<NullScope>();
    if (context.isModuleInfoNullMarked()) {
      nullScopes.add(NullScope.NULL_MARKED);
    }
    nullScopes.add(context.getPackageNullScope(thisClazz.packageName()));
    for (var outerClass : outerClasses) {
      nullScopes.add(context.getClassNullScope(outerClass.name()));
    }
    nullScopes.add(context.getClassNullScope(thisClazz.name()));
    return nullScopes;
  }

  private static NullScope getEffectiveNullMarkedScope(List<NullScope> nullScopes) {
    // from inner to outer
    for (var i = nullScopes.size() - 1; i >= 0; i--) {
      var scope = nullScopes.get(i);
      if (scope != NullScope.NOT_DEFINED) {
        return scope;
      }
    }
    return NullScope.NULL_UNMARKED;
  }

}
