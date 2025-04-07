package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public class ExplicitNullMarkedScopeCheck implements Check {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedScopeCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkModule() {
    // NOP
  }

  @Override
  public void checkPackage(VisitedPackage visitedPackage, BiConsumer<Kind, String> addIssue) {
    // NOP
  }

  @Override
  public void checkClass(VisitedClass visitedClass, AddIssue addIssue) {
    if (!visitedClass.annotations().contains(NullScopeAnnotation.NULL_MARKED)) {
      addIssue.addIssueForClass(
          Kind.MISSING_NULL_MARKED,
          messageSolver.missingNullMarkedClass()
      );
    }
  }
}
