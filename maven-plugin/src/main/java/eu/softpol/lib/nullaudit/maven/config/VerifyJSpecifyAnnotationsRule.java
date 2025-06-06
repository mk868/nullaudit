package eu.softpol.lib.nullaudit.maven.config;

import org.jspecify.annotations.NullMarked;

/**
 * Configuration for the &lt;verifyJSpecifyAnnotations&gt; rule.
 * <p>
 * This rule is used to validate the correct usage of JSpecify annotations within the codebase. It
 * ensures that the annotations provided by JSpecify are properly implemented, helping to maintain a
 * consistent and clear nullability standard across the project.
 */
@NullMarked
public class VerifyJSpecifyAnnotationsRule extends BaseRule {

}
