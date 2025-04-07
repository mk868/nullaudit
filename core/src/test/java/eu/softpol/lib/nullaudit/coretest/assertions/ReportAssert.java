package eu.softpol.lib.nullaudit.coretest.assertions;

import eu.softpol.lib.nullaudit.core.report.Report;
import org.assertj.core.api.AbstractAssert;

public class ReportAssert extends AbstractAssert<ReportAssert, Report> {

  protected ReportAssert(Report report) {
    super(report, ReportAssert.class);
  }

  public ReportAssert totalNumberOfIssues(int expected) {
    isNotNull();
    var issues = actual.issues();
    if (issues.size() != expected) {
      failWithMessage("Expected number of issues to be <%d> but was <%d>", expected, issues.size());
    }
    return this;
  }

  public ReportAssert hasNoIssues() {
    isNotNull();
    var issues = actual.issues();
    if (!issues.isEmpty()) {
      failWithMessage("Expected no issues but found <%d>", issues.size());
    }
    return this;
  }

  public ReportIssuesAssert issues() {
    return new ReportIssuesAssert(actual.issues());
  }

  public ReportIssuesAssert issuesForPackage(String packageName) {
    isNotNull();
    var filteredIssues = actual.issues().stream()
        .filter(issue -> issue.location().equals(packageName + ".package-info"))
        .toList();
    return new ReportIssuesAssert(filteredIssues);
  }

  public ReportIssuesAssert issuesForClass(String packageName, String className) {
    isNotNull();
    var filteredIssues = actual.issues().stream()
        .filter(issue -> issue.location().equals(packageName + "." + className))
        .toList();
    return new ReportIssuesAssert(filteredIssues);
  }

  public ReportIssuesAssert issuesForField(String packageName, String className, String fieldName) {
    isNotNull();
    var filteredIssues = actual.issues().stream()
        .filter(issue -> issue.location().equals(packageName + "." + className + "#" + fieldName))
        .toList();
    return new ReportIssuesAssert(filteredIssues);
  }

  public ReportIssuesAssert issuesForMethod(String packageName, String className,
      String methodDesc) {
    isNotNull();
    var filteredIssues = actual.issues().stream()
        .filter(issue -> issue.location().equals(packageName + "." + className + "#" + methodDesc))
        .toList();
    return new ReportIssuesAssert(filteredIssues);
  }
}
