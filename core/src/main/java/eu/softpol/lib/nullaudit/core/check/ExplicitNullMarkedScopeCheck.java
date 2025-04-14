package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
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
  public void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue) {
    // NOP
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
