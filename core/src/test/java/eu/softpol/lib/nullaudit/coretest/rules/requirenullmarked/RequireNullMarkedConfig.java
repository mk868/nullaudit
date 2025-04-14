package eu.softpol.lib.nullaudit.coretest.rules.requirenullmarked;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked;

public class RequireNullMarkedConfig {

  public static final NullAuditConfig CONFIG = NullAuditConfig.of()
      .withRequireNullMarked(new RequireNullMarked(Exclusions.empty()));
}
