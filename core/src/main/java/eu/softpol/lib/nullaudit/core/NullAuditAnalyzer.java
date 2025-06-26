package eu.softpol.lib.nullaudit.core;

import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireSpecifiedNullness;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.VerifyJSpecifyAnnotations;
import eu.softpol.lib.nullaudit.core.analyzer.ClassFileAnalyzer;
import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData;
import eu.softpol.lib.nullaudit.core.analyzer.ReportBuilder;
import eu.softpol.lib.nullaudit.core.check.Checker;
import eu.softpol.lib.nullaudit.core.check.CheckerFactory;
import eu.softpol.lib.nullaudit.core.check.IgnoreClassDecorator;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Report;
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

    var codeAnalysisData = new CodeAnalysisData();
    var fileAnalyzer = new ClassFileAnalyzer(codeAnalysisData, config.excludedPackages(),
        toCheckers(config));
    if (Files.isDirectory(input)) {
      new DirSource(input).analyze(fileAnalyzer);
    } else if (input.getFileName().toString().endsWith(".jar")) {
      new JarSource(input).analyze(fileAnalyzer);
    } else {
      throw new RuntimeException("Unsupported file type: %s".formatted(input));
    }

    return ReportBuilder.build(codeAnalysisData);
  }

  private static List<Checker> toCheckers(NullAuditConfig config) {
    var messageSolver = new MessageSolver();
    var result = new ArrayList<Checker>();
    Optional.ofNullable(config.verifyJSpecifyAnnotations()).ifPresent(c -> {
      List<Checker> checkers = CheckerFactory.createVerifyJSpecifyAnnotations(messageSolver);
      if (!c.exclusions().isEmpty()) {
        checkers = IgnoreClassDecorator.of(checkers, c.exclusions());
      }
      result.addAll(checkers);
    });
    Optional.ofNullable(config.requireNullMarked()).ifPresent(c -> {
      List<Checker> checkers = CheckerFactory.createRequireNullMarked(messageSolver);
      if (!c.exclusions().isEmpty()) {
        checkers = IgnoreClassDecorator.of(checkers, c.exclusions());
      }
      result.addAll(checkers);
    });
    Optional.ofNullable(config.requireSpecifiedNullness()).ifPresent(c -> {
      List<Checker> checkers = CheckerFactory.createRequireSpecifiedNullness(messageSolver);
      if (!c.exclusions().isEmpty()) {
        checkers = IgnoreClassDecorator.of(checkers, c.exclusions());
      }
      result.addAll(checkers);
    });

    return List.copyOf(result);
  }
}
