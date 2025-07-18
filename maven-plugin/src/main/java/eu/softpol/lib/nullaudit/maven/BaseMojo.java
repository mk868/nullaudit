package eu.softpol.lib.nullaudit.maven;

import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.ExclusionsFileParser;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked.On;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;
import eu.softpol.lib.nullaudit.maven.config.BaseRule;
import eu.softpol.lib.nullaudit.maven.config.RequireNullMarkedRule;
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
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An abstract Mojo that provides shared functionality for Maven plugin goals.
 * <p>
 * Subclasses of this class define specific behavior for goals like analyzing for errors or
 * generating reports.
 */
@NullMarked
public abstract class BaseMojo extends AbstractMojo {

  protected final MessageSolver messageSolver = new MessageSolver();

  @Parameter(defaultValue = "${project}", readonly = true)
  private @Nullable MavenProject project;


  /**
   * Indicates whether to skip processing and execution of the goal when the Maven project packaging
   * type is "pom".
   *
   * <ul>
   *   <li>If true, the tasks will be skipped when the project packaging type is "pom".</li>
   *   <li>If false, the tasks will proceed even if the project packaging type is "pom".</li>
   * </ul>
   * <p>
   * This parameter is particularly useful for Maven builds that do not require operations to be
   * performed on aggregation projects or parent POMs.
   * <p>
   * The default value is "true".
   */
  @Parameter(defaultValue = "true")
  private boolean skipPomPackaging;

  /**
   * Indicates whether to skip processing and execution of the goal when the Maven project packaging
   * type is "tile".
   *
   * <ul>
   *   <li>If true, the tasks will be skipped when the project packaging type is "tile".</li>
   *   <li>If false, the tasks will proceed even if the project packaging type is "tile".</li>
   * </ul>
   * <p>
   * The default value is "true".
   */
  @Parameter(defaultValue = "true")
  private boolean skipTilePackaging;

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
  private @Nullable RulesConfig rules;

  public boolean isSkipPomPackaging() {
    return skipPomPackaging;
  }

  protected boolean hasPackagingPom() {
    if (project == null || project.getFile() == null) {
      // run without pom.xml
      return false;
    }
    return project.getPackaging().equals("pom");
  }

  public boolean isSkipTilePackaging() {
    return skipTilePackaging;
  }

  protected boolean hasPackagingTile() {
    if (project == null || project.getFile() == null) {
      // run without pom.xml
      return false;
    }
    return project.getPackaging().equals("tile");
  }

  protected Path getInput() {
    return input;
  }

  protected RulesConfig getRulesOrDefault() {
    if (rules != null) {
      return rules;
    }
    var rulesProp = Arrays.stream(System.getProperty("nullaudit.rules", "")
            .split(","))
        .filter(not(String::isEmpty))
        .toList();
    if (!rulesProp.isEmpty()) {
      return new RulesConfig(
          rulesProp.contains("requireNullMarked") ? new RequireNullMarkedRule() : null,
          rulesProp.contains("requireSpecifiedNullness") ? new RequireSpecifiedNullnessRule()
              : null,
          rulesProp.contains("verifyJSpecifyAnnotations") ? new VerifyJSpecifyAnnotationsRule()
              : null
      );
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
                toExclusions(r)
            ))
            .orElse(null),
        Optional.ofNullable(rules.getRequireNullMarked())
            .filter(BaseRule::isActive)
            .map(r -> new RequireNullMarked(
                toExclusions(r),
                On.fromText(r.getOn())
            ))
            .orElse(null),
        Optional.ofNullable(rules.getRequireSpecifiedNullness())
            .filter(BaseRule::isActive)
            .map(r -> new RequireSpecifiedNullness(
                toExclusions(r)
            ))
            .orElse(null)
    );
  }

  private static Exclusions toExclusions(BaseRule rule) {
    var classes = Optional.ofNullable(rule.getExclusionsFile())
        .filter(not(String::isBlank))
        .map(BaseMojo::readClassExclusionsFile)
        .orElse(Set.of());
    var annotations = Optional.ofNullable(rule.getExcludeAnnotations())
        .filter(not(String::isBlank))
        .map(s -> Arrays.stream(s.split(","))
            .map(String::trim)
            .filter(not(String::isBlank))
            .collect(Collectors.toUnmodifiableSet()))
        .orElse(Set.of());
    return new Exclusions(classes, annotations);
  }

  private static Set<String> readClassExclusionsFile(String exclusionsFile) {
    try {
      return ExclusionsFileParser.parse(Path.of(exclusionsFile));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
