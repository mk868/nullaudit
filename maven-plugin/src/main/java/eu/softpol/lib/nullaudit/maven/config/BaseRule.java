package eu.softpol.lib.nullaudit.maven.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseRule {

  /**
   * Indicates whether the rule is active or not. Default value is true, meaning the rule is enabled
   * by default.
   */
  @Parameter(defaultValue = "true")
  private boolean active = true;

  /**
   * Comma-separated list of fully-qualified annotation names that cause the rule to skip the
   * annotated element and, optionally, its members.
   *
   * <p>
   * Typical use-case: exclude generated code by listing annotations such as
   * {@code javax.annotation.Generated} or {@code jakarta.annotation.Generated}.
   *
   * <p>If {@code null} or empty, no annotation-based exclusions are applied.</p>
   */
  @Parameter
  private @Nullable String excludeAnnotations;

  /**
   * A file listing classes (or patterns) to ignore for this rule.
   */
  @Parameter
  private @Nullable String exclusionsFile;

  /**
   * Classes (or patterns) to ignore for this rule, defined inline, one per line. Uses the same
   * format as the {@link #exclusionsFile} content (blank lines and {@code #} comments are ignored).
   * Can be combined with {@code exclusionsFile}.
   *
   * <pre>{@code
   * <exclusions>
   *   com.example.legacy.LegacyUser
   *   com.example.internal.**
   * </exclusions>
   * }</pre>
   */
  @Parameter
  private @Nullable String exclusions;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public @Nullable String getExcludeAnnotations() {
    return excludeAnnotations;
  }

  public void setExcludeAnnotations(@Nullable String excludeAnnotations) {
    this.excludeAnnotations = excludeAnnotations;
  }

  public @Nullable String getExclusionsFile() {
    return exclusionsFile;
  }

  public void setExclusionsFile(String exclusionsFile) {
    this.exclusionsFile = exclusionsFile;
  }

  public @Nullable String getExclusions() {
    return exclusions;
  }

  public void setExclusions(@Nullable String exclusions) {
    this.exclusions = exclusions;
  }
}
