package eu.softpol.lib.nullaudit.core;

import java.util.List;
import org.jspecify.annotations.Nullable;

public record NullAuditConfig(
    List<String> excludedPackages,
    @Nullable CheckJSpecifyUsage checkJSpecifyUsage,
    @Nullable RequireNullMarked requireNullMarked,
    @Nullable RequireSpecifiedNullness requireSpecifiedNullness
) {

  public record CheckJSpecifyUsage(
      @Nullable IgnoredClasses ignoredClasses
  ) {

  }

  public record RequireNullMarked(
      @Nullable IgnoredClasses ignoredClasses
  ) {

  }

  public record RequireSpecifiedNullness(
      @Nullable IgnoredClasses ignoredClasses
  ) {

  }

  public static NullAuditConfig of() {
    return new NullAuditConfig(List.of(), null, null, null);
  }

  public static NullAuditConfig of(List<String> excludedPackages) {
    return new NullAuditConfig(excludedPackages, null, null, null);
  }

}
