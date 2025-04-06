package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;

public class ExplicitNullMarkedScopeCheck implements Check {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedScopeCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(VisitedClass visitedClass, AddIssue addIssue) {
    if (!visitedClass.annotations().contains(NullScopeAnnotation.NULL_MARKED)) {
      addIssue.addIssueForClass(
          List.of(Kind.MISSING_NULL_MARKED),
          messageSolver.missingNullMarkedClass()
      );
    }
  }
}
