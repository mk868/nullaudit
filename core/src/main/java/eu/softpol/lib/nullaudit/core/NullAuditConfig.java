package eu.softpol.lib.nullaudit.core;

import java.util.List;
import org.jspecify.annotations.Nullable;

public record NullAuditConfig(
    List<String> excludedPackages,
    @Nullable VerifyJSpecifyAnnotations verifyJSpecifyAnnotations,
    @Nullable RequireNullMarked requireNullMarked,
    @Nullable RequireSpecifiedNullness requireSpecifiedNullness
) {

  public sealed interface Rule {

  }

  public record VerifyJSpecifyAnnotations(
      Exclusions exclusions
  ) implements Rule {

  }

  public record RequireNullMarked(
      Exclusions exclusions,
      On on
  ) implements Rule {

    public enum On {
      CLASS,
      PACKAGE
    }
  }

  public record RequireSpecifiedNullness(
      Exclusions exclusions
  ) implements Rule {

  }

  public static NullAuditConfig of() {
    return new NullAuditConfig(List.of(), null, null, null);
  }

  public static NullAuditConfig of(List<String> excludedPackages) {
    return new NullAuditConfig(excludedPackages, null, null, null);
  }

  public NullAuditConfig withVerifyJSpecifyAnnotations(@Nullable VerifyJSpecifyAnnotations value) {
    return new NullAuditConfig(this.excludedPackages, value, this.requireNullMarked,
        this.requireSpecifiedNullness);
  }

  public NullAuditConfig withRequireNullMarked(@Nullable RequireNullMarked value) {
    return new NullAuditConfig(this.excludedPackages, this.verifyJSpecifyAnnotations, value,
        this.requireSpecifiedNullness);
  }

  public NullAuditConfig withRequireSpecifiedNullness(@Nullable RequireSpecifiedNullness value) {
    return new NullAuditConfig(this.excludedPackages, this.verifyJSpecifyAnnotations,
        this.requireNullMarked, value);
  }

}
