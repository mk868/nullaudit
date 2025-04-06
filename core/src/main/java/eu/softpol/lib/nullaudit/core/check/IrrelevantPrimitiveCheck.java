package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.List;
import java.util.function.BiConsumer;

public class IrrelevantPrimitiveCheck implements Check {

  private final MessageSolver messageSolver;

  public IrrelevantPrimitiveCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkModule() {
    // NOP
  }

  @Override
  public void checkPackage(VisitedPackage visitedPackage, BiConsumer<List<Kind>, String> addIssue) {
    // NOP
  }

  @Override
  public void checkClass(VisitedClass visitedClass, AddIssue addIssue) {
    for (var componentInfo : visitedClass.components()) {
      if (isPrimitiveAnnotated(componentInfo.fs())) {
        addIssue.addIssueForField(componentInfo.componentName(),
            List.of(Kind.IRRELEVANT_ANNOTATION_ON_PRIMITIVE),
            messageSolver.issueIrrelevantAnnotationOnPrimitiveComponent()
        );
      }
    }

    if (!visitedClass.isRecord()) {
      for (var fieldInfo : visitedClass.fields()) {
        if (isPrimitiveAnnotated(fieldInfo.fs())) {
          addIssue.addIssueForField(fieldInfo.fieldName(),
              List.of(Kind.IRRELEVANT_ANNOTATION_ON_PRIMITIVE),
              messageSolver.issueIrrelevantAnnotationOnPrimitiveField()
          );
        }
      }
    }

    visitedClass.methods().stream()
        .filter(visitedMethod ->
            // not default component getter
            !visitedClass.isRecord() || !visitedMethod.isConstructor())
        .filter(visitedMethod ->
            // not default constructor
            !visitedClass.isRecord() || visitedClass.getComponent(visitedMethod.methodName())
                .isEmpty())
        .forEach(visitedMethod -> {
          if (isPrimitiveAnnotated(visitedMethod.ms().returnType()) ||
              visitedMethod.ms().parameterTypes().stream()
                  .anyMatch(IrrelevantPrimitiveCheck::isPrimitiveAnnotated)) {

            addIssue.addIssueForMethod(visitedMethod.descriptiveMethodName(),
                List.of(Kind.IRRELEVANT_ANNOTATION_ON_PRIMITIVE),
                messageSolver.issueIrrelevantAnnotationOnPrimitiveMethod()
            );
          }
        });
  }

  private static boolean isPrimitiveAnnotated(TypeNode type) {
    if (type instanceof PrimitiveTypeNode ptn) {
      return isAnnotated(ptn);
    }
    return type.getChildren().stream().anyMatch(IrrelevantPrimitiveCheck::isPrimitiveAnnotated);
  }

  private static boolean isAnnotated(PrimitiveTypeNode type) {
    return type.getAnnotations().contains(TypeUseAnnotation.JSPECIFY_NULLABLE) ||
           type.getAnnotations().contains(TypeUseAnnotation.JSPECIFY_NON_NULL);
  }
}
