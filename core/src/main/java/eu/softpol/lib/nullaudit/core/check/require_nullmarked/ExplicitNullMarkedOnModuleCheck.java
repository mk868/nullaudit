package eu.softpol.lib.nullaudit.core.check.require_nullmarked;

import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData.IssueEntry;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.ModuleLocation;
import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.check.ModuleInfoCheckContext;
import eu.softpol.lib.nullaudit.core.check.ModuleInfoChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class ExplicitNullMarkedOnModuleCheck implements ModuleInfoChecker, ClassChecker {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedOnModuleCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkModule(ModuleInfoCheckContext context) {
    var naModule = context.naModule();
    if (!naModule.annotations().contains(NAAnnotation.NULL_MARKED)) {
      context.addIssue(
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_MODULE)
      );
    }
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    if (context.naModule() != null) {
      return;
    }
    // no module-info.class - report it once
    var moduleLocation = new ModuleLocation(null);
    var alreadyExist = context.getIssues(moduleLocation).stream()
        .map(IssueEntry::kind)
        .anyMatch(Kind.MISSING_NULL_MARKED_ANNOTATION::equals);
    if (!alreadyExist) {
      context.addIssue(
          moduleLocation,
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_MODULE_INFO_MISSING)
      );
    }
  }
}
