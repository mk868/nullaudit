package eu.softpol.lib.nullaudit.core.report;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

  List<Issue> issues = new ArrayList<>();

  public void addIssue(Issue issue) {
    issues.add(issue);
  }

  public Report build() {
    return new Report(List.copyOf(issues));
  }
}
