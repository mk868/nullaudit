package eu.softpol.lib.nullaudit.core;

import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;

public record NullAuditConfig(
    List<String> excludedPackages,
    @Nullable VerifyJSpecifyAnnotations verifyJSpecifyAnnotations,
    @Nullable RequireNullMarked requireNullMarked,
    @Nullable RequireSpecifiedNullness requireSpecifiedNullness,
    @Nullable ProhibitNonJSpecifyAnnotations prohibitNonJSpecifyAnnotations
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
      PACKAGE;

      public static On fromText(String text) {
        return Arrays.stream(values())
            .filter(x -> x.name().equalsIgnoreCase(text))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown 'on' value: " + text));
      }
    }
  }

  public record RequireSpecifiedNullness(
      Exclusions exclusions
  ) implements Rule {

  }

  public record ProhibitNonJSpecifyAnnotations(
      Exclusions exclusions
  ) implements Rule {

  }

  public static NullAuditConfig of() {
    return new NullAuditConfig(List.of(), null, null, null, null);
  }

  public static NullAuditConfig of(List<String> excludedPackages) {
    return new NullAuditConfig(excludedPackages, null, null, null, null);
  }

  public NullAuditConfig withVerifyJSpecifyAnnotations(@Nullable VerifyJSpecifyAnnotations value) {
    return new NullAuditConfig(this.excludedPackages, value, this.requireNullMarked,
        this.requireSpecifiedNullness, this.prohibitNonJSpecifyAnnotations);
  }

  public NullAuditConfig withRequireNullMarked(@Nullable RequireNullMarked value) {
    return new NullAuditConfig(this.excludedPackages, this.verifyJSpecifyAnnotations, value,
        this.requireSpecifiedNullness, this.prohibitNonJSpecifyAnnotations);
  }

  public NullAuditConfig withRequireSpecifiedNullness(@Nullable RequireSpecifiedNullness value) {
    return new NullAuditConfig(this.excludedPackages, this.verifyJSpecifyAnnotations,
        this.requireNullMarked, value, this.prohibitNonJSpecifyAnnotations);
  }

  public NullAuditConfig withProhibitNonJSpecifyAnnotations(@Nullable ProhibitNonJSpecifyAnnotations value) {
    return new NullAuditConfig(this.excludedPackages, this.verifyJSpecifyAnnotations,
        this.requireNullMarked, this.requireSpecifiedNullness, value);
  }

}
