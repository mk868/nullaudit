package eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class TypeUseAnnotationsOnClassCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public TypeUseAnnotationsOnClassCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();
    if (naClass.annotations().contains(NAAnnotation.NULLABLE)) {
      context.addIssueForClass(
          Kind.NULLABLE_ON_CLASS,
          messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLABLE_ON_CLASS)
      );
    }
    if (naClass.annotations().contains(NAAnnotation.NON_NULL)) {
      context.addIssueForClass(
          Kind.NON_NULL_ON_CLASS,
          messageSolver.resolve(MessageKey.ISSUE_INVALID_NONNULL_ON_CLASS)
      );
    }
  }
}
