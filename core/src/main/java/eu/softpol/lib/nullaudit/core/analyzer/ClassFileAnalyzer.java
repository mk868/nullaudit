package eu.softpol.lib.nullaudit.core.analyzer;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.ModuleInfoClassVisitor;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.MyClassVisitor;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.PackageInfoClassVisitor;
import eu.softpol.lib.nullaudit.core.check.Check;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger.Level;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/**
 * Analyze project structure basis on the .class files.
 */
public class ClassFileAnalyzer implements FileAnalyzer {

  private static final System.Logger logger = System.getLogger(ClassFileAnalyzer.class.getName());

  private final AnalysisContext context = new AnalysisContext();
  private final ReportBuilder reportBuilder;
  private final List<String> excludePackages;

  public ClassFileAnalyzer(ReportBuilder reportBuilder, List<String> excludePackages,
      List<Check> checks) {
    this.reportBuilder = reportBuilder;
    this.excludePackages = List.copyOf(excludePackages);

    context.getChecks().addAll(checks);
  }

  @Override
  public boolean analyze(String fileName, InputStreamSupplier iss) {
    try {
      if (fileName.equals("module-info.class")) {
        analyze(iss, new ModuleInfoClassVisitor(context));
        return true;
      }
      if (fileName.equals("package-info.class")) {
        analyze(iss, new PackageInfoClassVisitor(context, reportBuilder));
        return true;
      }
      if (fileName.endsWith(".class")) {
        analyze(iss, new MyClassVisitor(context, reportBuilder));
        return true;
      }
    } catch (IOException e) {
      logger.log(Level.ERROR, "Failed to analyze class file: " + e.getMessage(), e);
      throw new UncheckedIOException(e);
    } catch (RuntimeException e) {
      logger.log(Level.ERROR, "Failed to analyze class file: " + fileName, e);
    }
    return false;
  }

  private void analyze(InputStreamSupplier iss, ClassVisitor classVisitor) throws IOException {
    try (var is = iss.get()) {
      ClassReader reader = new ClassReader(is);
      var className = reader.getClassName();
      var packageName = getPackageName(className);
      if (excludePackages.contains(packageName)) {
        logger.log(Level.DEBUG, "Skipping file: " + className);
        return;
      }
      reader.accept(classVisitor, 0);
    }
  }
}
