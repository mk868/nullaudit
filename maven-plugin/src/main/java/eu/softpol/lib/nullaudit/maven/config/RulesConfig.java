package eu.softpol.lib.nullaudit.maven.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Configuration for the rules used in the NullAudit analysis.
 * <p>
 * The class provides configurable options for rules that enforce specific checks.
 */
@NullMarked
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

  /**
   * This rule is used to prohibit the use of non-JSpecify annotations.
   */
  @Parameter
  private @Nullable ProhibitNonJSpecifyAnnotationsRule prohibitNonJSpecifyAnnotations;

  public RulesConfig() {
  }

  public RulesConfig(
      @Nullable RequireNullMarkedRule requireNullMarked,
      @Nullable RequireSpecifiedNullnessRule requireSpecifiedNullness,
      @Nullable VerifyJSpecifyAnnotationsRule verifyJSpecifyAnnotations,
      @Nullable ProhibitNonJSpecifyAnnotationsRule prohibitNonJSpecifyAnnotations
  ) {
    this.requireNullMarked = requireNullMarked;
    this.requireSpecifiedNullness = requireSpecifiedNullness;
    this.verifyJSpecifyAnnotations = verifyJSpecifyAnnotations;
    this.prohibitNonJSpecifyAnnotations = prohibitNonJSpecifyAnnotations;
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

  public @Nullable ProhibitNonJSpecifyAnnotationsRule getProhibitNonJSpecifyAnnotations() {
    return prohibitNonJSpecifyAnnotations;
  }

  public void setProhibitNonJSpecifyAnnotations(
      @Nullable ProhibitNonJSpecifyAnnotationsRule prohibitNonJSpecifyAnnotations) {
    this.prohibitNonJSpecifyAnnotations = prohibitNonJSpecifyAnnotations;
  }
}
