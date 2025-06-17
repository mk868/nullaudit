package eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;

public class IrrelevantPrimitiveCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public IrrelevantPrimitiveCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    for (var componentInfo : naClass.components()) {
      if (isPrimitiveAnnotated(componentInfo.fs())) {
        addIssue.addIssueForComponent(componentInfo,
            Kind.INVALID_NULLNESS_ON_PRIMITIVE,
            messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_COMPONENT)
        );
      }
    }

    if (!naClass.isRecord()) {
      for (var fieldInfo : naClass.fields()) {
        if (isPrimitiveAnnotated(fieldInfo.fs())) {
          addIssue.addIssueForField(fieldInfo,
              Kind.INVALID_NULLNESS_ON_PRIMITIVE,
              messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_FIELD)
          );
        }
      }
    }

    naClass.methods().stream()
        .filter(naMethod ->
            // not default component getter
            !naClass.isRecord() || !naMethod.isConstructor())
        .filter(naMethod ->
            // not default constructor
            !naClass.isRecord() || naClass.getComponent(naMethod.methodName())
                .isEmpty())
        .forEach(naMethod -> {
          if (isPrimitiveAnnotated(naMethod.ms().returnType()) ||
              naMethod.ms().parameterTypes().stream()
                  .anyMatch(IrrelevantPrimitiveCheck::isPrimitiveAnnotated)) {

            addIssue.addIssueForMethod(naMethod,
                Kind.INVALID_NULLNESS_ON_PRIMITIVE,
                messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_METHOD)
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
