package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
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
    if (!naClass.annotations().contains(NullScopeAnnotation.NULL_MARKED)) {
      addIssue.addIssueForClass(
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.missingNullMarkedAnnotationClass()
      );
    }
  }
}
