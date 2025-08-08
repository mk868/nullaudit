package eu.softpol.lib.nullaudit.core.check.require_nullmarked;

import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData.IssueEntry;
import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.check.PackageInfoCheckContext;
import eu.softpol.lib.nullaudit.core.check.PackageInfoChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class ExplicitNullMarkedOnPackageCheck implements PackageInfoChecker, ClassChecker {

  private final MessageSolver messageSolver;

  public ExplicitNullMarkedOnPackageCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkPackage(PackageInfoCheckContext context) {
    var naPackage = context.naPackage();
    if (!naPackage.annotations().contains(NAAnnotation.NULL_MARKED)) {
      context.addIssue(
          Kind.MISSING_NULL_MARKED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_PACKAGE)
      );
    }
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naPackage = context.naPackage();
    var naClass = context.naClass();
    if (naClass.outerClass() != null) {
      return;
    }
    if (naPackage == null || !naPackage.annotations().contains(NAAnnotation.NULL_MARKED)) {
      var packageLocation = context.location().packageLocation();
      var alreadyExist = context.getIssues(packageLocation).stream()
          .map(IssueEntry::kind)
          .anyMatch(Kind.MISSING_NULL_MARKED_ANNOTATION::equals);
      if (!alreadyExist) {
        context.addIssue(
            packageLocation,
            Kind.MISSING_NULL_MARKED_ANNOTATION,
            messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_PACKAGE)
        );
      }
    }
  }
}
