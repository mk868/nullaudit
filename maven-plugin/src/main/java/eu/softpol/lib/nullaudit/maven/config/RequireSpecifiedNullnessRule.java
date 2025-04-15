package eu.softpol.lib.nullaudit.maven.config;

/**
 * Configuration for the &lt;requireSpecifiedNullness&gt; rule.
 * <p>
 * This rule enforces the requirement that nullness must be explicitly specified in the codebase. It
 * ensures that all fields, methods, and parameters are annotated with specific nullability
 * annotations, enhancing clarity and preventing unknown-nullness-related issues.
 */
public class RequireSpecifiedNullnessRule extends BaseRule {

}
