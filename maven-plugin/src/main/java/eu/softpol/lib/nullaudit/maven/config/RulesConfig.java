package eu.softpol.lib.nullaudit.maven.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.Nullable;

/**
 * Represents the <rules> block in the configuration. It holds each specific rule as an object
 * field.
 */
public class RulesConfig {

  /**
   * This rule ensures that the codebase uses the @NullMarked annotation to explicitly indicate
   * nullability scope.
   */
  @Parameter
  private @Nullable RequireNullMarkedRule requireNullMarked;

  /**
   * This rule enforces that the code explicitly specify nullability.
   */
  @Parameter
  private @Nullable RequireSpecifiedNullnessRule requireSpecifiedNullness;

  /**
   * This rule is used to validate the proper usage of the JSpecify annotations.
   */
  @Parameter
  private @Nullable VerifyJSpecifyAnnotationsRule verifyJSpecifyAnnotations;

  public RulesConfig() {
  }

  public RulesConfig(
      @Nullable RequireNullMarkedRule requireNullMarked,
      @Nullable RequireSpecifiedNullnessRule requireSpecifiedNullness,
      @Nullable VerifyJSpecifyAnnotationsRule verifyJSpecifyAnnotations
  ) {
    this.requireNullMarked = requireNullMarked;
    this.requireSpecifiedNullness = requireSpecifiedNullness;
    this.verifyJSpecifyAnnotations = verifyJSpecifyAnnotations;
  }

  public @Nullable RequireNullMarkedRule getRequireNullMarked() {
    return requireNullMarked;
  }

  public void setRequireNullMarked(@Nullable RequireNullMarkedRule requireNullMarked) {
    this.requireNullMarked = requireNullMarked;
  }

  public @Nullable RequireSpecifiedNullnessRule getRequireSpecifiedNullness() {
    return requireSpecifiedNullness;
  }

  public void setRequireSpecifiedNullness(@Nullable RequireSpecifiedNullnessRule requireSpecifiedNullness) {
    this.requireSpecifiedNullness = requireSpecifiedNullness;
  }

  public @Nullable VerifyJSpecifyAnnotationsRule getVerifyJSpecifyAnnotations() {
    return verifyJSpecifyAnnotations;
  }

  public void setVerifyJSpecifyAnnotations(@Nullable VerifyJSpecifyAnnotationsRule verifyJSpecifyAnnotations) {
    this.verifyJSpecifyAnnotations = verifyJSpecifyAnnotations;
  }
}
