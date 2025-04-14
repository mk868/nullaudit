package eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;

public class RequireSpecifiedNullnessConfig {

  public static final NullAuditConfig CONFIG = NullAuditConfig.of()
      .withRequireSpecifiedNullness(new RequireSpecifiedNullness(Exclusions.empty()));
}
