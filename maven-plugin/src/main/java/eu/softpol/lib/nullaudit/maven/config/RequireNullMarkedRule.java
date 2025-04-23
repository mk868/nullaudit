package eu.softpol.lib.nullaudit.maven.config;

import org.jspecify.annotations.NullMarked;

/**
 * Configuration for the &lt;requireNullMarked&gt; rule.
 * <p>
 * This rule ensures that the codebase uses the @NullMarked annotation on classes to explicitly
 * indicate nullability scope.
 */
@NullMarked
public class RequireNullMarkedRule extends BaseRule {

}
