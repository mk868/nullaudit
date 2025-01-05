package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getClassName;
import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.comparator.CheckOrder;
import eu.softpol.lib.nullaudit.core.report.ProblemEntry;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import eu.softpol.lib.nullaudit.core.signature.SignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MyClassVisitor extends org.objectweb.asm.ClassVisitor {

  private static final System.Logger logger = System.getLogger(MyClassVisitor.class.getName());

  private final AnalysisContext context;
  private final ReportBuilder reportBuilder;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private final List<MethodInfo> methods = new ArrayList<>();
  private String packageName = "";
  private String className = "";
  private String sourceFileName;

  public MyClassVisitor(AnalysisContext context, ReportBuilder reportBuilder) {
    super(Opcodes.ASM9);
    this.context = context;
    this.reportBuilder = reportBuilder;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    packageName = getPackageName(name);
    className = getClassName(name);
    super.visit(version, access, name, signature, superName, interfaces);
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
  public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor,
      @Nullable String methodSignature, String[] exceptions) {
    if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
      return super.visitMethod(access, methodName, methodDescriptor, methodSignature, exceptions);
    }

    final MethodSignature ms;
    try {
      ms = SignatureAnalyzer.analyze(methodSignature == null ? methodDescriptor : methodSignature);
    } catch (RuntimeException e) {
      throw new RuntimeException(
          "Reading signature of " + methodName + methodDescriptor + " (" + methodSignature
          + ") failed", e);
    }
    var parameterTypes = ms.parameterTypes();

    if (className.contains("$")) {
      // it's probably inner class
      var outerClassName = className.substring(0, className.lastIndexOf("$"));
      // check if the first constructor's argument has outer class type
      if (methodName.equals("<init>") &&
          !parameterTypes.isEmpty() &&
          parameterTypes.get(0).getValue().equals(outerClassName)) {
        parameterTypes.get(0).setOperator(NullnessOperator.MINUS_NULL);
      }
      // alternatively, I can search for the ACC_SYNTHETIC field of outerClassName type, but the compiler can skip this field when it's not used.
    }

    var methodInfo = new MethodInfo(methodName, methodDescriptor, methodSignature, ms);
    methods.add(methodInfo);

    return new MyMethodVisitor(methodInfo);
  }

  @Override
  public void visitEnd() {
    context.setClassNullScope(className, NullScope.from(annotations));

    for (var methodInfo : methods) {
      var classNullScope = new HashMap<String, NullScope>();
      classNullScope.put(className, NullScope.from(annotations));
      var tmp = className;
      while (tmp.contains("$")) {
        tmp = tmp.substring(0, tmp.lastIndexOf("$"));
        classNullScope.put(tmp, context.getClassNullScope(tmp));
      }
      var effectiveNullMarkedScope = getEffectiveNullMarkedScope(
          context.isModuleInfoNullMarked(),
          context.getPackageNullScope(packageName),
          classNullScope,
          NullScope.from(methodInfo.annotations())
      );

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
          appendProblemEntry(
              methodInfo.methodName(),
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

    super.visitEnd();
  }

  private void appendProblemEntry(String method, String message) {
    reportBuilder.addProblemEntry(
        context.getModuleName(),
        className,
        new ProblemEntry(
            method,
            message
        )
    );
  }

  private static NullScope getEffectiveNullMarkedScope(
      boolean isModuleNullMarked,
      NullScope packageNullScope,
      Map<String, NullScope> classNullScope,
      NullScope methodNullScope
  ) {
    if (methodNullScope != NullScope.NOT_DEFINED) {
      return methodNullScope;
    }
    // from inner to outer
    var sorted = classNullScope.entrySet().stream()
        .sorted(Map.Entry.comparingByKey(CheckOrder.COMPARATOR.reversed()))
        .map(Map.Entry::getValue)
        .toList();
    for (var scope : sorted) {
      if (scope != NullScope.NOT_DEFINED) {
        return scope;
      }
    }
    if (packageNullScope != NullScope.NOT_DEFINED) {
      return packageNullScope;
    }
    return isModuleNullMarked ? NullScope.NULL_MARKED : NullScope.NULL_UNMARKED;
  }

}
