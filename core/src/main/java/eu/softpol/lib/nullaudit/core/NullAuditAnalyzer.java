package eu.softpol.lib.nullaudit.core;

import eu.softpol.lib.nullaudit.core.NullAuditConfig.CheckJSpecifyUsage;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.analyzer.ClassFileAnalyzer;
import eu.softpol.lib.nullaudit.core.check.Check;
import eu.softpol.lib.nullaudit.core.check.ExplicitNullMarkedScopeCheck;
import eu.softpol.lib.nullaudit.core.check.IgnoreClassDecorator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NullAuditAnalyzer {

  private final Path input;
  private final NullAuditConfig config;

  public NullAuditAnalyzer(Path input, NullAuditConfig config) {
    this.input = input;
    this.config = config;
  }

  @Deprecated
  public NullAuditAnalyzer(Path input, List<String> excludedPackages) {
    this(input, new NullAuditConfig(
        excludedPackages,
        new CheckJSpecifyUsage(null),
        null,
        new RequireSpecifiedNullness(null)
    ));
  }

  public Report run() {
    if (!Files.exists(input)) {
      throw new RuntimeException("File %s does not exist.".formatted(input));
    }

    var reportBuilder = new ReportBuilder();
    var fileAnalyzer = new ClassFileAnalyzer(reportBuilder, config.excludedPackages(),
        toChecks(config));
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

  private static List<Check> toChecks(NullAuditConfig config) {
    var messageSolver = new MessageSolver();
    var result = new ArrayList<Check>();
    Optional.ofNullable(config.checkJSpecifyUsage()).ifPresent(c -> {
      List<Check> checks = List.of(
          new IrrelevantMarkedCheck(messageSolver),
          new IrrelevantPrimitiveCheck(messageSolver)
      );
      if (c.ignoredClasses() != null) {
        checks = checks.stream()
            .map(x -> (Check) new IgnoreClassDecorator(x, c.ignoredClasses()))
            .toList();
      }
      result.addAll(checks);
    });
    Optional.ofNullable(config.requireNullMarked()).ifPresent(c -> {
      Check check = new ExplicitNullMarkedScopeCheck(messageSolver);
      if (c.ignoredClasses() != null) {
        check = new IgnoreClassDecorator(check, c.ignoredClasses());
      }
      result.add(check);
    });
    Optional.ofNullable(config.requireSpecifiedNullness()).ifPresent(c -> {
      Check check = new UnspecifiedNullnessCheck(messageSolver);
      if (c.ignoredClasses() != null) {
        check = new IgnoreClassDecorator(check, c.ignoredClasses());
      }
      result.add(check);
    });

    return List.copyOf(result);
  }

  private double calculateCoveragePercentage(int totalClasses, int unspecifiedNullnessClasses) {
    return Math.round(1000.0 * (totalClasses - unspecifiedNullnessClasses) / totalClasses) / 10.0;
  }
}
