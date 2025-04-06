package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static java.util.Objects.requireNonNullElse;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.MutableVisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.MutableVisitedMethod;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedComponent;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedField;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.check.Check.AddIssue;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import eu.softpol.lib.nullaudit.core.signature.FieldSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.MethodSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
  private final MessageSolver messageSolver;
  private final ReportBuilder reportBuilder;
  private final List<Clazz> outerClasses = new ArrayList<>();
  private String sourceFileName;
  private MutableVisitedClass visitedClass;

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
    visitedClass = new MutableVisitedClass(
        Clazz.of(name),
        Clazz.of(superName)
    );
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName,
      int access) {
    if (outerName != null && visitedClass.thisClazz().internalName().startsWith(name)) {
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
    var annotationOpt = KnownAnnotations.fromDescriptor(descriptor)
        .map(x -> switch (x) {
          case NULL_MARKED -> NullScopeAnnotation.NULL_MARKED;
          case NULL_UNMARKED -> NullScopeAnnotation.NULL_UNMARKED;
          case KOTLIN_METADATA -> NullScopeAnnotation.KOTLIN_METADATA;
          default -> null;
        });
    if (annotationOpt.isPresent()) {
      visitedClass.addAnnotation(annotationOpt.get());
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public RecordComponentVisitor visitRecordComponent(String name, String descriptor,
      @Nullable String signature) {

    var fs = FieldSignatureAnalyzer.analyze(requireNonNullElse(signature, descriptor));
    var visitedComponent = new VisitedComponent(name, descriptor, signature, fs);
    visitedClass.addComponent(visitedComponent);

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
    visitedClass.addField(visitedField);

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
        classTypeNode.addAnnotation(TypeUseAnnotation.JSPECIFY_NON_NULL); // TODO should not be here
      }
      // alternatively, I can search for the ACC_SYNTHETIC field of outerClassName type, but the compiler can skip this field when it's not used.
    }

    String descriptiveMethodName;
    if (methodName.equals("<init>")) {
      descriptiveMethodName = visitedClass.thisClazz().simpleName();
    } else {
      descriptiveMethodName = methodName;
    }
    descriptiveMethodName += Arrays.stream(Type.getArgumentTypes(methodDescriptor))
        .map(Type::getClassName)
        .collect(Collectors.joining(", ", "(", ")"));

    var methodInfo = new MutableVisitedMethod(methodName, descriptiveMethodName, methodDescriptor,
        methodSignature, ms);
    visitedClass.addMethod(methodInfo);

    return new MyMethodVisitor(methodInfo);
  }

  @Override
  public void visitEnd() {
    context.setClassNullScope(visitedClass.thisClazz().name(),
        NullScope.from(visitedClass.annotations()));
    var nullScopes = getNullScopesForClass();

    reportBuilder.incSummaryTotalClasses();
    boolean unspecifiedNullnessFound = false;

    visitedClass.setEffectiveNullScope(getEffectiveNullMarkedScope(nullScopes));

    visitedClass.methods().stream()
        .map(MutableVisitedMethod.class::cast)
        .forEach(vm -> vm.setEffectiveNullScope(getEffectiveNullMarkedScope(
            List.of(visitedClass.effectiveNullScope(), NullScope.from(vm.annotations())))));

    var issuesForClass = new HashMap<String, List<Kind>>();
    context.getChecks()
        .forEach(c -> c.checkClass(visitedClass, new AddIssue() {
          @Override
          public void addIssueForClass(List<Kind> kinds, String message) {
            MyClassVisitor.this.appendIssue(kinds, message);
          }

          @Override
          public void addIssueForField(String name, List<Kind> kinds, String message) {
            MyClassVisitor.this.appendIssue(name, kinds, message);
            issuesForClass.computeIfAbsent(name, k -> new ArrayList<>())
                .addAll(kinds);
          }

          @Override
          public void addIssueForMethod(String name, List<Kind> kinds, String message) {
            MyClassVisitor.this.appendIssue(name, kinds, message);
            issuesForClass.computeIfAbsent(name, k -> new ArrayList<>())
                .addAll(kinds);
          }
        }));

    for (var componentInfo : visitedClass.components()) {
      reportBuilder.incSummaryTotalFields();

      if (issuesForClass.getOrDefault(componentInfo.componentName(), List.of())
          .contains(Kind.UNSPECIFIED_NULLNESS)) {
        reportBuilder.incSummaryUnspecifiedNullnessFields();
        unspecifiedNullnessFound = true;
      }
    }

    for (var fieldInfo : visitedClass.fields()) {
      if (visitedClass.isRecord()) {
        // generated by compiler - ignore
        continue;
      }
      reportBuilder.incSummaryTotalFields();

      if (issuesForClass.getOrDefault(fieldInfo.fieldName(), List.of())
          .contains(Kind.UNSPECIFIED_NULLNESS)) {
        reportBuilder.incSummaryUnspecifiedNullnessFields();
        unspecifiedNullnessFound = true;
      }
    }

    for (var methodInfo : visitedClass.methods()) {
      reportBuilder.incSummaryTotalMethods();
      if (issuesForClass.getOrDefault(methodInfo.descriptiveMethodName(), List.of())
          .contains(Kind.UNSPECIFIED_NULLNESS)) {
        reportBuilder.incSummaryUnspecifiedNullnessMethods();
        unspecifiedNullnessFound = true;
      }
    }

    if (unspecifiedNullnessFound) {
      reportBuilder.incSummaryUnspecifiedNullnessClasses();
    }

    super.visitEnd();
  }

  private void appendIssue(@Nullable String name, List<Kind> kinds, String message) {
    var location = "";
    if (context.getModuleName() != null) {
      location = context.getModuleName() + "/";
    }
    location += visitedClass.thisClazz().name();
    if (name != null) {
      location += "#" + name;
    }

    reportBuilder.addIssue(new Issue(
        location,
        kinds,
        message
    ));
  }

  private void appendIssue(List<Kind> kinds, String message) {
    appendIssue(null, kinds, message);
  }

  private List<NullScope> getNullScopesForClass() {
    var nullScopes = new ArrayList<NullScope>();
    if (context.isModuleInfoNullMarked()) {
      nullScopes.add(NullScope.NULL_MARKED);
    }
    nullScopes.add(context.getPackageNullScope(visitedClass.thisClazz().packageName()));
    for (var outerClass : outerClasses) {
      nullScopes.add(context.getClassNullScope(outerClass.name()));
    }
    nullScopes.add(context.getClassNullScope(visitedClass.thisClazz().name()));
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
