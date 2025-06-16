package eu.softpol.lib.nullaudit.core;

import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;
import eu.softpol.lib.nullaudit.core.analyzer.ClassFileAnalyzer;
import eu.softpol.lib.nullaudit.core.check.Checker;
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
        new VerifyJSpecifyAnnotations(Exclusions.empty()),
        null,
        new RequireSpecifiedNullness(Exclusions.empty())
    ));
  }

  public Report run() {
    if (!Files.exists(input)) {
      throw new RuntimeException("File %s does not exist.".formatted(input));
    }

    var reportBuilder = new ReportBuilder();
    var fileAnalyzer = new ClassFileAnalyzer(reportBuilder, config.excludedPackages(),
        toCheckers(config));
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

  private static List<Checker> toCheckers(NullAuditConfig config) {
    var messageSolver = new MessageSolver();
    var result = new ArrayList<Checker>();
    Optional.ofNullable(config.verifyJSpecifyAnnotations()).ifPresent(c -> {
      List<Checker> checkers = List.of(
          new IrrelevantMarkedCheck(messageSolver),
          new IrrelevantPrimitiveCheck(messageSolver)
      );
      if (!c.exclusions().isEmpty()) {
        checkers = checkers.stream()
            .map(x -> (Checker) new IgnoreClassDecorator(x, c.exclusions()))
            .toList();
      }
      result.addAll(checkers);
    });
    Optional.ofNullable(config.requireNullMarked()).ifPresent(c -> {
      Checker checkers = new ExplicitNullMarkedScopeCheck(messageSolver);
      if (!c.exclusions().isEmpty()) {
        checkers = new IgnoreClassDecorator(checkers, c.exclusions());
      }
      result.add(checkers);
    });
    Optional.ofNullable(config.requireSpecifiedNullness()).ifPresent(c -> {
      Checker checker = new UnspecifiedNullnessCheck(messageSolver);
      if (!c.exclusions().isEmpty()) {
        checker = new IgnoreClassDecorator(checker, c.exclusions());
      }
      result.add(checker);
    });

    return List.copyOf(result);
  }

  private double calculateCoveragePercentage(int totalClasses, int unspecifiedNullnessClasses) {
    return Math.round(1000.0 * (totalClasses - unspecifiedNullnessClasses) / totalClasses) / 10.0;
  }
}
