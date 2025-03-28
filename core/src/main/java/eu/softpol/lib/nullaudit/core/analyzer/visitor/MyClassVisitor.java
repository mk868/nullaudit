package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedComponent;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedField;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedMethod;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.FieldSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.signature.MethodSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.translator.AugmentedStringTranslator;
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
  private final MessageSolver messageSolver;
  private final ReportBuilder reportBuilder;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private final List<VisitedComponent> components = new ArrayList<>();
  private final List<VisitedField> fields = new ArrayList<>();
  private final List<VisitedMethod> methods = new ArrayList<>();
  private Clazz superClazz;
  private final List<Clazz> outerClasses = new ArrayList<>();
  private Clazz thisClazz;
  private String sourceFileName;

  public MyClassVisitor(AnalysisContext context, MessageSolver messageSolver,
      ReportBuilder reportBuilder) {
    super(Opcodes.ASM9);
    this.context = context;
    this.messageSolver = messageSolver;
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
  public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName,
      int access) {
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
    var annotationOpt = KnownAnnotations.fromDescriptor(descriptor);
    if (annotationOpt.isPresent()) {
      switch (annotationOpt.get()) {
        case NULL_MARKED -> annotations.add(NullScopeAnnotation.NULL_MARKED);
        case NULL_UNMARKED -> annotations.add(NullScopeAnnotation.NULL_UNMARKED);
        case KOTLIN_METADATA -> annotations.add(NullScopeAnnotation.KOTLIN_METADATA);
      }
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
      @Nullable String signature) {

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var visitedComponent = new VisitedComponent(name, descriptor, signature, fs);
    components.add(visitedComponent);

    return new MyRecordComponentVisitor(visitedComponent);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String descriptor,
      @Nullable String signature, Object value) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitField(access, name, descriptor, signature, value);
    }

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var visitedField = new VisitedField(name, descriptor, signature, fs);
    fields.add(visitedField);

    return new MyFieldVisitor(visitedField);
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

    if (!outerClasses.isEmpty()) {
      // it's probably inner class
      var outerClassName = outerClasses.get(outerClasses.size() - 1).name();
      // check if the first constructor's argument has outer class type
      if (methodName.equals("<init>") &&
          !parameterTypes.isEmpty() &&
          parameterTypes.get(0) instanceof ClassTypeNode classTypeNode &&
          classTypeNode.getClazz().equals(outerClassName)) {
        classTypeNode.setOperator(NullnessOperator.MINUS_NULL);
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

    var methodInfo = new VisitedMethod(methodName, descriptiveMethodName, methodDescriptor,
        methodSignature, ms);
    methods.add(methodInfo);

    return new MyMethodVisitor(methodInfo);
  }

  @Override
  public void visitEnd() {
    context.setClassNullScope(thisClazz.name(), NullScope.from(annotations));

    reportBuilder.incSummaryTotalClasses();
    boolean unspecifiedNullnessFound = false;

    if (annotations.containsAll(
        List.of(NullScopeAnnotation.NULL_MARKED, NullScopeAnnotation.NULL_UNMARKED))) {
      appendIssue(
          List.of(Kind.IRRELEVANT_ANNOTATION),
          messageSolver.issueIrrelevantAnnotationNullUnMarkedClass()
      );
    }

    for (var componentInfo : components) {
      reportBuilder.incSummaryTotalFields();

      var nullScopes = getNullScopesForClass();
      var effectiveNullMarkedScope = getEffectiveNullMarkedScope(nullScopes);

      if (effectiveNullMarkedScope != NullScope.NULL_MARKED) {
        var s = "%s %s".formatted(
            AugmentedStringTranslator.INSTANCE.translate(componentInfo.fs()),
            componentInfo.componentName()
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessFields();
          appendIssue(
              componentInfo.componentName(),
              List.of(Kind.UNSPECIFIED_NULLNESS),
              messageSolver.issueUnspecifiedNullnessComponent(
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
            AugmentedStringTranslator.INSTANCE.translate(fieldInfo.fs()),
            fieldInfo.fieldName()
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessFields();
          appendIssue(
              fieldInfo.fieldName(),
              List.of(Kind.UNSPECIFIED_NULLNESS),
              messageSolver.issueUnspecifiedNullnessField(
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }

    for (var methodInfo : methods) {
      reportBuilder.incSummaryTotalMethods();

      if (methodInfo.annotations().containsAll(
          List.of(NullScopeAnnotation.NULL_MARKED, NullScopeAnnotation.NULL_UNMARKED))) {
        appendIssue(
            methodInfo.descriptiveMethodName(),
            List.of(Kind.IRRELEVANT_ANNOTATION),
            messageSolver.issueIrrelevantAnnotationNullUnMarkedMethod()
        );
      }

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
                .anyMatch(c -> AugmentedStringTranslator.INSTANCE.translate(c.fs())
                    .equals(
                        AugmentedStringTranslator.INSTANCE.translate(methodInfo.ms().returnType())))
            && methodInfo.ms().parameterTypes().isEmpty()
        ) {
          // skip default getter
          continue;
        }
        if (methodName.equals("<init>") && methodInfo.ms().parameterTypes().stream()
            .map(AugmentedStringTranslator.INSTANCE::translate)
            .collect(Collectors.joining(",")).equals(components.stream()
                .map(VisitedComponent::fs)
                .map(AugmentedStringTranslator.INSTANCE::translate)
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
            AugmentedStringTranslator.INSTANCE.translate(methodInfo.ms().returnType()),
            methodInfo.methodName(),
            methodInfo.ms()
                .parameterTypes().stream()
                .map(AugmentedStringTranslator.INSTANCE::translate)
                .collect(Collectors.joining(", "))
        );
        if (s.contains("*")) {
          unspecifiedNullnessFound = true;
          reportBuilder.incSummaryUnspecifiedNullnessMethods();
          appendIssue(
              methodInfo.descriptiveMethodName(),
              List.of(Kind.UNSPECIFIED_NULLNESS),
              messageSolver.issueUnspecifiedNullnessMethod(
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

  private void appendIssue(String name, List<Kind> kinds, String message) {
    var location = "";
    if (context.getModuleName() != null) {
      location = context.getModuleName() + "/";
    }
    location += thisClazz.name();
    location += "#" + name;

    reportBuilder.addIssue(new Issue(
        location,
        kinds,
        message
    ));
  }

  private void appendIssue(List<Kind> kinds, String message) {
    var location = "";
    if (context.getModuleName() != null) {
      location = context.getModuleName() + "/";
    }
    location += thisClazz.name();

    reportBuilder.addIssue(new Issue(
        location,
        kinds,
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
