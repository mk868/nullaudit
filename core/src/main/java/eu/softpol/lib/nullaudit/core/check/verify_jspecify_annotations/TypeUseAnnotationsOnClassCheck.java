package eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class TypeUseAnnotationsOnClassCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public TypeUseAnnotationsOnClassCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    if (naClass.annotations().contains(KnownAnnotations.NULLABLE)) {
      addIssue.addIssueForClass(
          Kind.NULLABLE_ON_CLASS,
         messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLABLE_ON_CLASS)
      );
    }
    if (naClass.annotations().contains(KnownAnnotations.NON_NULL)) {
      addIssue.addIssueForClass(
          Kind.NON_NULL_ON_CLASS,
         messageSolver.resolve(MessageKey.ISSUE_INVALID_NONNULL_ON_CLASS)
      );
    }
  }
}
