package eu.softpol.lib.nullaudit.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
      Set<On> on
  ) implements Rule {

    public RequireNullMarked {
      if (on.isEmpty()) {
        throw new IllegalArgumentException("At least one 'on' value is required");
      }
      on = Collections.unmodifiableSet(EnumSet.copyOf(on));
    }

    public RequireNullMarked(Exclusions exclusions, On on) {
      this(exclusions, EnumSet.of(on));
    }

    public enum On {
      CLASS,
      PACKAGE,
      MODULE;

      public static On fromText(String text) {
        var trimmed = text.trim();
        return Arrays.stream(values())
            .filter(x -> x.name().equalsIgnoreCase(trimmed))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown 'on' value: '%s'. Allowed values: %s".formatted(
                    trimmed,
                    Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(", "))
                )));
      }

      /**
       * Parses values joined with {@code +}, e.g. {@code "MODULE+PACKAGE"}.
       */
      public static Set<On> parse(String text) {
        if (text.isBlank()) {
          throw new IllegalArgumentException("At least one 'on' value is required");
        }
        return Collections.unmodifiableSet(Arrays.stream(text.split("\\+", -1))
            .map(On::fromText)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(On.class))));
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
