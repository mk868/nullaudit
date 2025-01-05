package eu.softpol.lib.nullaudit.maven;

import static java.util.function.Predicate.not;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class BaseMojo extends AbstractMojo {

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

  protected Path getInput() {
    return input;
  }

  protected List<String> getExcludedPackages() {
    if (excludePackageNames != null && !excludePackageNames.isEmpty()) {
      return Arrays.stream(excludePackageNames.split("[,:;]"))
          .map(String::trim)
          .filter(not(String::isBlank))
          .toList();
    }

    return List.of();
  }
}
