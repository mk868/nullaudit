package eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.check.PackageInfoCheckContext;
import eu.softpol.lib.nullaudit.core.check.PackageInfoChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;

public class IrrelevantMarkedCheck implements ClassChecker, PackageInfoChecker {

  private static final List<NAAnnotation> INVALID_ANNOTATION_COMBINATION = List.of(
      NAAnnotation.NULL_MARKED,
      NAAnnotation.NULL_UNMARKED
  );
  private final MessageSolver messageSolver;

  public IrrelevantMarkedCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkPackage(PackageInfoCheckContext context) {
    var annotations = context.naPackage().annotations();
    if (annotations.containsAll(INVALID_ANNOTATION_COMBINATION)) {
      context.addIssue(
          Kind.INVALID_NULL_MARK_COMBINATION,
          messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_PACKAGE)
      );
    }
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();
    if (naClass.annotations().containsAll(INVALID_ANNOTATION_COMBINATION)) {
      context.addIssueForClass(
          Kind.INVALID_NULL_MARK_COMBINATION,
          messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_CLASS)
      );
    }

    naClass.methods().forEach(naMethod -> {
      if (naMethod.annotations().containsAll(INVALID_ANNOTATION_COMBINATION)) {
        context.addIssueForMethod(
            naMethod,
            Kind.INVALID_NULL_MARK_COMBINATION,
            messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_METHOD)
        );
      }
    });
  }
}
