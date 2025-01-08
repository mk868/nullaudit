package eu.softpol.lib.nullaudit.maven;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import java.util.Arrays;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The `check` goal performing null-audit checks on a specified input, such as a directory or JAR
 * file.
 * <p>
 * The analysis result is logged on the stdout, and a configurable property determines whether the
 * build process should fail if problems are detected.
 * <p>
 * This goal can be used without the Maven pom.xml project file.
 */
@SuppressWarnings("unused")
@Mojo(name = "check", requiresProject = false)
public class CheckMojo extends BaseMojo {


  /**
   * Determines whether the build process should fail if null-audit analysis detects any problems.
   */
  @Parameter(property = "nullaudit.failOnError", defaultValue = "true")
  private boolean failOnError;

  public void execute() throws MojoExecutionException {
    var analyze = new NullAuditAnalyzer(getInput(), getExcludedPackages());
    var report = analyze.run();

    if (report.issues().isEmpty()) {
      getLog().info("No issues found.");
    } else {
      getLog().error("%d issues found.".formatted(report.issues().size()));

      for (var issue : report.issues()) {
        var message = "%s: %s".formatted(
            issue.location(),
            issue.message()
        );
        var lines = message.split("\n");
        getLog().error(lines[0]);
        Arrays.stream(lines)
            .skip(1)
            .map(l -> "    " + l)
            .forEach(getLog()::error);
      }

      if (failOnError) {
        throw new MojoExecutionException("%d issues found.".formatted(report.issues().size()));
      }
    }
  }
}
