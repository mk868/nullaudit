package eu.softpol.lib.nullaudit.core;

import eu.softpol.lib.nullaudit.core.analyzer.ClassFileAnalyzer;
import eu.softpol.lib.nullaudit.core.check.IrrelevantMarkedCheck;
import eu.softpol.lib.nullaudit.core.check.IrrelevantPrimitiveCheck;
import eu.softpol.lib.nullaudit.core.check.UnspecifiedNullnessCheck;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Report;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import eu.softpol.lib.nullaudit.core.source.DirSource;
import eu.softpol.lib.nullaudit.core.source.JarSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class NullAuditAnalyzer {

  private final Path input;
  private final List<String> excludePackages;

  public NullAuditAnalyzer(Path input, List<String> excludePackages) {
    this.input = input;
    this.excludePackages = List.copyOf(excludePackages);
  }

  public Report run() {
    if (!Files.exists(input)) {
      throw new RuntimeException("File %s does not exist.".formatted(input));
    }

    var messageSolver = new MessageSolver();
    var reportBuilder = new ReportBuilder();
    var fileAnalyzer = new ClassFileAnalyzer(reportBuilder, excludePackages, List.of(
        new IrrelevantMarkedCheck(messageSolver),
        new IrrelevantPrimitiveCheck(messageSolver),
        new UnspecifiedNullnessCheck(messageSolver)
    ));
    if (Files.isDirectory(input)) {
      new DirSource(input).analyze(fileAnalyzer);
    } else if (input.getFileName().toString().endsWith(".jar")) {
      new JarSource(input).analyze(fileAnalyzer);
    } else {
      throw new RuntimeException("Unsupported file type: %s".formatted(input));
    }

    reportBuilder.setCoveragePercentage(calculateCoveragePercentage(
        reportBuilder.getSummaryTotalClasses(),
        reportBuilder.getSummaryUnspecifiedNullnessClasses()
    ));
    return reportBuilder.build();
  }

  private double calculateCoveragePercentage(int totalClasses, int unspecifiedNullnessClasses) {
    return Math.round(1000.0 * (totalClasses - unspecifiedNullnessClasses) / totalClasses) / 10.0;
  }
}
