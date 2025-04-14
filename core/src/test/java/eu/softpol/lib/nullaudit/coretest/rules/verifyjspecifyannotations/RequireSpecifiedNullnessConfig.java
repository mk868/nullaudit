package eu.softpol.lib.nullaudit.coretest.rules.verifyjspecifyannotations;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;

public class RequireSpecifiedNullnessConfig {

  public static final NullAuditConfig CONFIG = NullAuditConfig.of()
      .withVerifyJSpecifyAnnotations(new VerifyJSpecifyAnnotations(Exclusions.empty()));
}
