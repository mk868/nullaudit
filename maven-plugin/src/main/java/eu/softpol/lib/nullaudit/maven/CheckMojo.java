package eu.softpol.lib.nullaudit.maven;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.maven.i18n.MessageKey;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.NullMarked;

/**
 * The `check` goal performing null-audit checks on a specified input, such as a directory or JAR
 * file.
 * <p>
 * The analysis result is logged on the stdout, and a configurable property determines whether the
 * build process should fail if problems are detected.
 * <p>
 * This goal can be used without the Maven pom.xml project file.
 */
@NullMarked
@SuppressWarnings("unused")
@Mojo(name = "check", requiresProject = false)
public class CheckMojo extends BaseMojo {

  /**
   * Determines whether the build process should fail if null-audit analysis detects any problems.
   */
  @Parameter(property = "nullaudit.failOnError", defaultValue = "true")
  private boolean failOnError;

  /**
   * Limit the number of issues displayed on the console.
   */
  @Parameter(property = "nullaudit.maxErrors", defaultValue = "-1")
  private int maxErrors;

  public void execute() throws MojoExecutionException {
    if (isSkipPomPackaging() && hasPackagingPom()) {
      getLog().info(messageSolver.resolve(MessageKey.SKIPPING_POM_PACKAGING));
      return;
    }

    if (isSkipTilePackaging() && hasPackagingTile()) {
      getLog().info(messageSolver.resolve(MessageKey.SKIPPING_TILE_PACKAGING));
      return;
    }

    var analyze = new NullAuditAnalyzer(getInput(), createConfig());
    var report = analyze.run();

    var issuesCount = report.issues().size();

    if (issuesCount == 0) {
      getLog().info(messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, 0));
    } else {
      getLog().error(messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, issuesCount));

      List<Issue> issues = report.issues();
      int issuesToShow = issuesCount;
      if (maxErrors > 0) {
        issuesToShow = Math.min(issuesToShow, maxErrors);
      }
      for (int i = 0; i < issuesToShow; i++) {
        var issue = issues.get(i);
        var message = "%s: %s".formatted(
            issue.location(),
            issue.message()
        );
        var lines = message.split("\n");
        Arrays.stream(lines).forEach(getLog()::error);
      }

      var issuesLeft = issuesCount - issuesToShow;
      if (issuesLeft > 0) {
        getLog().error(messageSolver.resolve(MessageKey.CHECK_MORE_ISSUES, issuesLeft));
      }

      if (failOnError) {
        throw new MojoExecutionException(
            messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, issuesCount)
        );
      }
    }
  }
}
