package eu.softpol.lib.nullaudit.maven;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.core.report.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jspecify.annotations.Nullable;

/**
 * The `report` goal generates a null-audit analysis report for a specified input, such as a
 * directory or a JAR file.
 * <p>
 * The build will not fail if problems are found during the analysis.
 * <p>
 * The report is saved to a specified directory and file in JSON format.
 * <p>
 * This goal can be used without the Maven pom.xml project file.
 */
@SuppressWarnings("unused")
@Mojo(name = "report", requiresProject = false)
public class ReportMojo extends BaseMojo {

  /**
   * Specifies the directory where the null-audit report will be saved.
   */
  @Parameter(property = "nullaudit.reportDirectory", defaultValue = "nullaudit")
  protected @Nullable String reportDirectory;
  /**
   * Specifies the name of the output file where the null-audit analysis report will be saved.
   */
  @Parameter(property = "nullaudit.reportFile", defaultValue = "report.json")
  private String reportFile;

  public void execute() throws MojoExecutionException {
    var analyze = new NullAuditAnalyzer(getInput(), getExcludedPackages());
    var report = analyze.run();

    final File outputReport;
    if (reportDirectory != null && !reportDirectory.isBlank()) {
      var parentDir = Path.of(reportDirectory);
      try {
        Files.createDirectories(parentDir);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      outputReport = parentDir.resolve(reportFile).toFile();
    } else {
      outputReport = new File(reportFile);
    }

    saveReport(report, outputReport);
    getLog().info("Report saved to " + outputReport);
  }

  private void saveReport(Report report, File file) {
    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    try (FileWriter writer = new FileWriter(file)) {
      gson.toJson(report, writer);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to save report.", e);
    }
  }
}