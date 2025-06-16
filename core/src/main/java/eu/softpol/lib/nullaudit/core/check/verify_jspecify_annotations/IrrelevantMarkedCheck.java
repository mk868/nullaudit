package eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.check.PackageInfoChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import java.util.function.BiConsumer;

public class IrrelevantMarkedCheck implements ClassChecker, PackageInfoChecker {

  private static final List<KnownAnnotations> INVALID_ANNOTATION_COMBINATION = List.of(
      KnownAnnotations.NULL_MARKED,
      KnownAnnotations.NULL_UNMARKED
  );
  private final MessageSolver messageSolver;

  public IrrelevantMarkedCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue) {
    if (naPackage.annotations().containsAll(INVALID_ANNOTATION_COMBINATION)) {
      addIssue.accept(
          Kind.INVALID_NULL_MARK_COMBINATION,
          messageSolver.invalidNullMarkCombinationPackage()
      );
    }
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    if (naClass.annotations().containsAll(INVALID_ANNOTATION_COMBINATION)) {
      addIssue.addIssueForClass(
          Kind.INVALID_NULL_MARK_COMBINATION,
          messageSolver.invalidNullMarkCombinationClass()
      );
    }

    naClass.methods().forEach(naMethod -> {
      if (naMethod.annotations().containsAll(INVALID_ANNOTATION_COMBINATION)) {
        addIssue.addIssueForMethod(
            naMethod,
            Kind.INVALID_NULL_MARK_COMBINATION,
            messageSolver.invalidNullMarkCombinationMethod()
        );
      }
    });
  }
}
