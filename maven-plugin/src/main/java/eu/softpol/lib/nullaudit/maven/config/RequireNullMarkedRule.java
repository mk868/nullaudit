package eu.softpol.lib.nullaudit.maven.config;

import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked.On;
import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.NullMarked;

/**
 * Configuration for the &lt;requireNullMarked&gt; rule.
 * <p>
 * This rule ensures that the codebase uses the @NullMarked annotation on classes to explicitly
 * indicate nullability scope.
 */
@NullMarked
public class RequireNullMarkedRule extends BaseRule {


  /**
   * Specifies the target location of the {@code @NullMarked} annotation. Determines whether the
   * {@code @NullMarked} annotation enforcement should apply to classes, or packages. The default
   * value is "CLASS".
   */
  @Parameter
  private String on = On.CLASS.name();

  public String getOn() {
    return on;
  }

  public void setOn(String on) {
    this.on = on;
  }
}
