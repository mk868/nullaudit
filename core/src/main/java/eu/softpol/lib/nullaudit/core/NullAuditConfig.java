package eu.softpol.lib.nullaudit.core;

import java.util.List;
import org.jspecify.annotations.Nullable;

public record NullAuditConfig(
    List<String> excludedPackages,
    @Nullable VerifyJSpecifyAnnotations verifyJSpecifyAnnotations,
    @Nullable RequireNullMarked requireNullMarked,
    @Nullable RequireSpecifiedNullness requireSpecifiedNullness
) {

  public record VerifyJSpecifyAnnotations(
      @Nullable Exclusions exclusions
  ) {

  }

  public record RequireNullMarked(
      @Nullable Exclusions exclusions
  ) {

  }

  public record RequireSpecifiedNullness(
      @Nullable Exclusions exclusions
  ) {

  }

  public static NullAuditConfig of() {
    return new NullAuditConfig(List.of(), null, null, null);
  }

  public static NullAuditConfig of(List<String> excludedPackages) {
    return new NullAuditConfig(excludedPackages, null, null, null);
  }

}
