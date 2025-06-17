package eu.softpol.lib.nullaudit.coretest.rules;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked.On;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;

public class RulesConfig {

  public static final NullAuditConfig VERIFY_JSPECIFY_ANNOTATIONS = NullAuditConfig.of()
      .withVerifyJSpecifyAnnotations(new VerifyJSpecifyAnnotations(Exclusions.empty()));
  public static final NullAuditConfig REQUIRE_SPECIFIED_NULLNESS = NullAuditConfig.of()
      .withRequireSpecifiedNullness(new RequireSpecifiedNullness(Exclusions.empty()));
  public static final NullAuditConfig REQUIRE_NULLMARKED_ON_CLASS = NullAuditConfig.of()
      .withRequireNullMarked(new RequireNullMarked(Exclusions.empty(), On.CLASS));
  public static final NullAuditConfig REQUIRE_NULLMARKED_ON_PACKAGE = NullAuditConfig.of()
      .withRequireNullMarked(new RequireNullMarked(Exclusions.empty(), On.PACKAGE));

}
