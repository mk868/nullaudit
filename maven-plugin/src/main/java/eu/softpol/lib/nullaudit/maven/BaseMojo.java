package eu.softpol.lib.nullaudit.maven;

import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.ExclusionsFileParser;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;
import eu.softpol.lib.nullaudit.maven.config.BaseRule;
import eu.softpol.lib.nullaudit.maven.config.RequireSpecifiedNullnessRule;
import eu.softpol.lib.nullaudit.maven.config.RulesConfig;
import eu.softpol.lib.nullaudit.maven.config.VerifyJSpecifyAnnotationsRule;
import eu.softpol.lib.nullaudit.maven.i18n.MessageSolver;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class BaseMojo extends AbstractMojo {

  protected final MessageSolver messageSolver = new MessageSolver();

  /**
   * Represents the input path for analysis. This variable specifies the location of the directory
   * or file (e.g., a .jar file) to be analyzed.
   */
  @Parameter(property = "nullaudit.input", defaultValue = "${project.build.outputDirectory}")
  private @Nullable Path input;

  /**
   * Specifies a list of package names to exclude from the analysis. Package names can be separated
   * by commas, colons, or semicolons.
   * <p>
   * If no value is provided, no packages will be excluded by default.
   */
  @Parameter(property = "nullaudit.excludePackageNames")
  private @Nullable String excludePackageNames;

  /**
   * Specifies the rules configurations used for NullAudit checks. This variable holds specific rule
   * settings that define different validation behaviors during the analysis process.
   */
  @Parameter
  private RulesConfig rules;

  protected Path getInput() {
    return input;
  }

  protected RulesConfig getRulesOrDefault() {
    if (rules != null) {
      return rules;
    }
    return new RulesConfig(
        null,
        new RequireSpecifiedNullnessRule(),
        new VerifyJSpecifyAnnotationsRule()
    );
  }

  private List<String> getExcludedPackages() {
    if (excludePackageNames != null && !excludePackageNames.isEmpty()) {
      return Arrays.stream(excludePackageNames.split("[,:;]"))
          .map(String::trim)
          .filter(not(String::isBlank))
          .toList();
    }

    return List.of();
  }

  protected NullAuditConfig createConfig() {
    var rules = getRulesOrDefault();

    return new NullAuditConfig(
        getExcludedPackages(),
        Optional.ofNullable(rules.getVerifyJSpecifyAnnotations())
            .filter(BaseRule::isActive)
            .map(r -> new VerifyJSpecifyAnnotations(
                Optional.ofNullable(r.getExclusionsFile())
                    .map(BaseMojo::toIgnoredClasses)
                    .orElse(null)
            ))
            .orElse(null),
        Optional.ofNullable(rules.getRequireNullMarked())
            .filter(BaseRule::isActive)
            .map(r -> new RequireNullMarked(
                Optional.ofNullable(r.getExclusionsFile())
                    .map(BaseMojo::toIgnoredClasses)
                    .orElse(null)
            ))
            .orElse(null),
        Optional.ofNullable(rules.getRequireSpecifiedNullness())
            .filter(BaseRule::isActive)
            .map(r -> new RequireSpecifiedNullness(
                Optional.ofNullable(r.getExclusionsFile())
                    .map(BaseMojo::toIgnoredClasses)
                    .orElse(null)
            ))
            .orElse(null)
    );
  }

  private static @Nullable Exclusions toIgnoredClasses(@Nullable String exclusionsFile) {
    if (exclusionsFile == null || exclusionsFile.isBlank()) {
      return null;
    }
    try {
      return ExclusionsFileParser.parse(Path.of(exclusionsFile));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
