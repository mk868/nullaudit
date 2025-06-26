package eu.softpol.lib.nullaudit.core.analyzer;

import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.ClassLocation;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.MemberLocation;
import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData.IssueEntry;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.report.Report;
import eu.softpol.lib.nullaudit.core.report.Summary;
import eu.softpol.lib.nullaudit.core.report.Summary.SummaryUnspecifiedNullness;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class ReportBuilder {

  public static Report build(CodeAnalysisData codeAnalysisData) {
    return new Report(
        createSummary(codeAnalysisData),
        createIssues(codeAnalysisData)
    );
  }

  private static List<Issue> createIssues(CodeAnalysisData codeAnalysisData) {
    var resultIssues = new ArrayList<Issue>();

    var packages = codeAnalysisData.issues().keySet().stream()
        .map(CodeLocation::packageName)
        .distinct()
        .sorted(String::compareTo)
        .toList();
    for (var p : packages) {
      // issues for package-info
      codeAnalysisData.issues().entrySet().stream()
          .filter(kv -> kv.getKey() instanceof CodeLocation.PackageLocation)
          .filter(kv -> kv.getKey().packageName().equals(p))
          .forEach(kv -> kv.getValue().forEach(ie -> resultIssues.add(toIssue(kv.getKey(), ie))));

      var classes = codeAnalysisData.issues().keySet().stream()
          .filter(l -> l.packageName().equals(p))
          .map(l -> {
            if (l instanceof ClassLocation cl) {
              return cl.className();
            } else if (l instanceof MemberLocation cl) {
              return cl.className();
            } else {
              return null;
            }
          })
          .filter(Objects::nonNull)
          .distinct()
          .sorted(String::compareTo)
          .toList();
      for (var c : classes) {
        codeAnalysisData.issues().entrySet().stream()
            .filter(kv -> kv.getKey() instanceof ClassLocation)
            .filter(kv -> kv.getKey().packageName().equals(p))
            .filter(kv -> ((ClassLocation) kv.getKey()).className().equals(c))
            .forEach(kv -> kv.getValue().forEach(ie -> resultIssues.add(toIssue(kv.getKey(), ie))));

        codeAnalysisData.issues().entrySet().stream()
            .filter(kv -> kv.getKey() instanceof MemberLocation)
            .filter(kv -> kv.getKey().packageName().equals(p))
            .filter(kv -> ((MemberLocation) kv.getKey()).className().equals(c))
            .forEach(kv -> kv.getValue().forEach(ie -> resultIssues.add(toIssue(kv.getKey(), ie))));
      }
    }
    return List.copyOf(resultIssues);
  }

  private static Summary createSummary(CodeAnalysisData codeAnalysisData) {
    var unspecifiedNullnessIssues = codeAnalysisData.issues().entrySet().stream()
        .filter(kv -> kv.getValue().stream().anyMatch(ie -> ie.kind() == Kind.UNSPECIFIED_NULLNESS))
        .map(Entry::getKey)
        .toList();

    var unspecifiedNullnessClasses = (int) unspecifiedNullnessIssues.stream()
        .map(c -> {
          if (c instanceof ClassLocation cl) {
            return c.packageName() + "-" + cl.className();
          } else if (c instanceof MemberLocation ml) {
            return c.packageName() + "-" + ml.className();
          } else {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .distinct()
        .count();
    var unspecifiedNullnessMethods = (int) unspecifiedNullnessIssues.stream()
        .filter(c -> c instanceof MemberLocation)
        .map(c -> (MemberLocation) c)
        .map(c -> c.packageName() + "-" + c.className() + "-" + c.memberName())
        .filter(a -> a.contains("("))
        .count();
    var unspecifiedNullnessFields = (int) unspecifiedNullnessIssues.stream()
        .filter(c -> c instanceof MemberLocation)
        .map(c -> (MemberLocation) c)
        .map(c -> c.packageName() + "-" + c.className() + "-" + c.memberName())
        .filter(not(a -> a.contains("(")))
        .count();

    var coveragePercentage = calculateCoveragePercentage(
        codeAnalysisData.summaryTotalClasses(),
        unspecifiedNullnessClasses
    );

    return new Summary(
        codeAnalysisData.summaryTotalClasses(),
        codeAnalysisData.summaryTotalMethods(),
        codeAnalysisData.summaryTotalFields(),
        new SummaryUnspecifiedNullness(
            unspecifiedNullnessClasses,
            unspecifiedNullnessMethods,
            unspecifiedNullnessFields
        ),
        coveragePercentage);
  }

  private static Issue toIssue(CodeLocation codeLocation, IssueEntry issueEntry) {
    String location = "";
    if (codeLocation.module() != null) {
      location += codeLocation.module() + "/";
    }
    if (!codeLocation.packageName().isEmpty()) {
      location += codeLocation.packageName() + ".";
    }
    if (codeLocation instanceof CodeLocation.PackageLocation __) {
      location += "package-info";
    } else if (codeLocation instanceof ClassLocation classLocation) {
      location += classLocation.className();
    } else if (codeLocation instanceof MemberLocation memberLocation) {
      location += memberLocation.className() + "#" + memberLocation.memberName();
    } else {
      throw new IllegalArgumentException("Unsupported code location: " + codeLocation);
    }

    return new Issue(
        location,
        issueEntry.kind(),
        issueEntry.message()
    );
  }

  private static double calculateCoveragePercentage(int totalClasses,
      int unspecifiedNullnessClasses) {
    return Math.round(1000.0 * (totalClasses - unspecifiedNullnessClasses) / totalClasses) / 10.0;
  }
}
