package eu.softpol.lib.nullaudit.maven.config;

import org.jspecify.annotations.NullMarked;

/**
 * Configuration for the &lt;prohibitNonJSpecifyAnnotations&gt; rule.
 * <p>
 * This rule detects usages of nullness annotations other than those provided by JSpecify (e.g.,
 * {@code javax.annotation.Nullable}, {@code org.jetbrains.annotations.NotNull}, etc.).
 * <p>
 * Enabling this rule helps to standardize nullness analysis by enforcing JSpecify annotations
 * exclusively across the codebase.
 */
@NullMarked
public class ProhibitNonJSpecifyAnnotationsRule extends BaseRule {

}
