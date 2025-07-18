package eu.softpol.lib.nullaudit.core.check.require_nullmarked;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.NAAnnotation;
import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class ExplicitNullMarkedOnClassCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedOnClassCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();
    if (naClass.outerClass() != null) {
      return;
    }
    if (!naClass.annotations().contains(NAAnnotation.NULL_MARKED)) {
      context.addIssueForClass(
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_CLASS)
      );
    }
  }
}
