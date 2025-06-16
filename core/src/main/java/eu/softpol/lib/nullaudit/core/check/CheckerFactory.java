package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.check.require_nullmarked.ExplicitNullMarkedScopeCheck;
import eu.softpol.lib.nullaudit.core.check.require_specified_nullness.UnspecifiedNullnessCheck;
import eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations.IrrelevantMarkedCheck;
import eu.softpol.lib.nullaudit.core.check.verify_jspecify_annotations.IrrelevantPrimitiveCheck;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import java.util.List;

public class CheckerFactory {

  public static List<Checker> createRequireSpecifiedNullness(MessageSolver messageSolver) {
    return List.of(new UnspecifiedNullnessCheck(messageSolver));
  }

  public static List<Checker> createVerifyJSpecifyAnnotations(MessageSolver messageSolver) {
    return List.of(
        new IrrelevantMarkedCheck(messageSolver),
        new IrrelevantPrimitiveCheck(messageSolver)
    );
  }

  public static List<Checker> createRequireNullMarked(MessageSolver messageSolver) {
    return List.of(new ExplicitNullMarkedScopeCheck(messageSolver));
  }
}
