package eu.softpol.lib.nullaudit.maven.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MessageSolver {

  private final ResourceBundle resourceBundle;

  public MessageSolver() {
    this.resourceBundle = ResourceBundle.getBundle("eu.softpol.lib.nullaudit.maven.messages");
  }

  public String checkNoIssuesFound() {
    return resourceBundle.getString("plugin.check.noIssuesFound");
  }

  public String checkOneIssueFound() {
    return resourceBundle.getString("plugin.check.oneIssueFound");
  }

  public String checkMultipleIssuesFound(int number) {
    return MessageFormat.format(
        resourceBundle.getString("plugin.check.multipleIssuesFound"),
        number
    );
  }

  public String checkOneMoreIssue() {
    return resourceBundle.getString("plugin.check.oneMoreIssue");
  }

  public String checkMultipleMoreIssues(int number) {
    return MessageFormat.format(
        resourceBundle.getString("plugin.check.multipleMoreIssues"),
        number
    );
  }

  public String reportReportSaved(String path) {
    return MessageFormat.format(
        resourceBundle.getString("plugin.report.reportSaved"),
        path
    );
  }

  public String skippingPomPackaging() {
    return resourceBundle.getString("plugin.common.skippingPomPackaging");
  }
}
