package eu.softpol.lib.nullaudit.core.report;

public record Summary(
    int totalClasses,
    int totalMethods,
    int totalFields,
    SummaryUnspecifiedNullness unspecifiedNullness,
    double coveragePercentage
    //TODO String mostProblematicPackage
) {

  public record SummaryUnspecifiedNullness(
      int classes,
      int methods,
      int fields
  ) {

  }


}
