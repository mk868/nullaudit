package eu.softpol.lib.nullaudit.core.analyzer;

import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeAnalysisData {

  private int summaryTotalClasses;
  private int summaryTotalMethods;
  private int summaryTotalFields;
  private final Map<CodeLocation, List<IssueEntry>> issues = new HashMap<>();

  public record IssueEntry(Kind kind, String message) {

  }

  public int summaryTotalClasses() {
    return summaryTotalClasses;
  }

  public int summaryTotalMethods() {
    return summaryTotalMethods;
  }

  public int summaryTotalFields() {
    return summaryTotalFields;
  }

  public Map<CodeLocation, List<IssueEntry>> issues() {
    return issues;
  }

  public void incSummaryTotalClasses() {
    summaryTotalClasses++;
  }

  public void incSummaryTotalMethods(int i) {
    summaryTotalMethods += i;
  }

  public void incSummaryTotalFields(int i) {
    summaryTotalFields += i;
  }

  public void addIssue(CodeLocation codeLocation, Kind kind, String message) {
    issues.computeIfAbsent(codeLocation, k -> new ArrayList<>())
        .add(new IssueEntry(kind, message));
  }

  public List<IssueEntry> getIssues(CodeLocation codeLocation) {
    return List.copyOf(issues.getOrDefault(codeLocation, List.of()));
  }
}
