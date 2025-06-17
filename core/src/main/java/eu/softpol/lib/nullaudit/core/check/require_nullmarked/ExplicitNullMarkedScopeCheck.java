package eu.softpol.lib.nullaudit.core.check.require_nullmarked;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class ExplicitNullMarkedScopeCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedScopeCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    if (naClass.outerClass() != null) {
      return;
    }
    if (!naClass.annotations().contains(KnownAnnotations.NULL_MARKED)) {
      addIssue.addIssueForClass(
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_CLASS)
      );
    }
  }
}
