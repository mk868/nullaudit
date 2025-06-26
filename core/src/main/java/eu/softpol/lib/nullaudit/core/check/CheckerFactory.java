package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked.On;
import eu.softpol.lib.nullaudit.core.check.require_nullmarked.ExplicitNullMarkedOnClassCheck;
import eu.softpol.lib.nullaudit.core.check.require_nullmarked.ExplicitNullMarkedOnPackageCheck;
import eu.softpol.lib.nullaudit.core.check.require_specified_nullness.UnspecifiedNullnessCheck;
import eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations.IrrelevantMarkedCheck;
import eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations.IrrelevantPrimitiveCheck;
import eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations.TypeUseAnnotationsOnClassCheck;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import java.util.List;

public class CheckerFactory {

  private final MessageSolver messageSolver;

  public CheckerFactory(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  public List<Checker> createRequireSpecifiedNullness() {
    return List.of(new UnspecifiedNullnessCheck(messageSolver));
  }

  public List<Checker> createVerifyJSpecifyAnnotations() {
    return List.of(
        new IrrelevantMarkedCheck(messageSolver),
        new IrrelevantPrimitiveCheck(messageSolver),
        new TypeUseAnnotationsOnClassCheck(messageSolver)
    );
  }

  public List<Checker> createRequireNullMarked(On on) {
    return switch (on) {
      case CLASS -> List.of(new ExplicitNullMarkedOnClassCheck(messageSolver));
      case PACKAGE -> List.of(new ExplicitNullMarkedOnPackageCheck(messageSolver));
    };
  }
}
