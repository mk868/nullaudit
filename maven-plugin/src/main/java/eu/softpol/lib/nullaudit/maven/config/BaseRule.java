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
   * A file listing classes (or patterns) to ignore for this rule.
   */
  @Parameter
  private @Nullable String exclusionsFile;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public @Nullable String getExclusionsFile() {
    return exclusionsFile;
  }

  public void setExclusionsFile(String exclusionsFile) {
    this.exclusionsFile = exclusionsFile;
  }
}
