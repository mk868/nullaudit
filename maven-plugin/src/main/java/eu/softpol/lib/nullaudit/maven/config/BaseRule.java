package eu.softpol.lib.nullaudit.maven.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.Nullable;

public abstract class BaseRule {

  /**
   * Indicates whether the rule is active or not. Default value is true, meaning the rule is enabled
   * by default.
   */
  @Parameter(defaultValue = "true")
  private boolean active = true;

  /**
   * A file listing classes (or patterns) to ignore for this rule.
   */
  @Parameter
  private @Nullable String ignoredClassesFile;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public @Nullable String getIgnoredClassesFile() {
    return ignoredClassesFile;
  }

  public void setIgnoredClassesFile(String ignoredClassesFile) {
    this.ignoredClassesFile = ignoredClassesFile;
  }
}
