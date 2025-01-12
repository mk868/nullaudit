package eu.softpol.lib.nullaudit.core.report;

import eu.softpol.lib.nullaudit.core.report.Summary.SummaryUnspecifiedNullness;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

  private int summaryTotalClasses;
  private int summaryTotalMethods;
  private int summaryTotalFields;
  private int summaryUnspecifiedNullnessClasses;
  private int summaryUnspecifiedNullnessMethods;
  private int summaryUnspecifiedNullnessFields;
  private double coveragePercentage;
  private final List<Issue> issues = new ArrayList<>();

  public int getSummaryTotalClasses() {
    return summaryTotalClasses;
  }

  public void incSummaryTotalClasses() {
    summaryTotalClasses++;
  }

  public void incSummaryTotalMethods() {
    summaryTotalMethods++;
  }

  public void incSummaryTotalFields() {
    summaryTotalFields++;
  }

  public int getSummaryUnspecifiedNullnessClasses() {
    return summaryUnspecifiedNullnessClasses;
  }

  public void incSummaryUnspecifiedNullnessClasses() {
    summaryUnspecifiedNullnessClasses++;
  }

  public void incSummaryUnspecifiedNullnessMethods() {
    summaryUnspecifiedNullnessMethods++;
  }

  public void incSummaryUnspecifiedNullnessFields() {
    summaryUnspecifiedNullnessFields++;
  }

  public void setCoveragePercentage(double coveragePercentage) {
    this.coveragePercentage = coveragePercentage;
  }

  public void addIssue(Issue issue) {
    issues.add(issue);
  }

  public Report build() {
    return new Report(
        new Summary(
            summaryTotalClasses,
            summaryTotalMethods,
            summaryTotalFields,
            new SummaryUnspecifiedNullness(
                summaryUnspecifiedNullnessClasses,
                summaryUnspecifiedNullnessMethods,
                summaryUnspecifiedNullnessFields
            ),
            coveragePercentage),
        List.copyOf(issues)
    );
  }
}
