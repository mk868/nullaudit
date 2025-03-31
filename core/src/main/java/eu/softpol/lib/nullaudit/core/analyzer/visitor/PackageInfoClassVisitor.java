package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.report.ReportBuilder;
import java.util.HashSet;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class PackageInfoClassVisitor extends ClassVisitor {

  private final AnalysisContext context;
  private final ReportBuilder reportBuilder;
  private VisitedPackage visitedPackage;

  public PackageInfoClassVisitor(AnalysisContext context, ReportBuilder reportBuilder) {
    super(Opcodes.ASM9);
    this.context = context;
    this.reportBuilder = reportBuilder;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    visitedPackage = new VisitedPackage(
        getPackageName(name),
        new HashSet<>()
    );
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    var annotation = KnownAnnotations.fromDescriptor(descriptor).orElse(null);
    if (annotation == KnownAnnotations.NULL_MARKED) {
      visitedPackage.annotations().add(NullScopeAnnotation.NULL_MARKED);
    }
    if (annotation == KnownAnnotations.NULL_UNMARKED) {
      visitedPackage.annotations().add(NullScopeAnnotation.NULL_UNMARKED);
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    var nullScope = NullScope.from(visitedPackage.annotations());
    context.setPackageNullScope(visitedPackage.packageName(), nullScope);
    context.getChecks().forEach(c -> c.checkPackage(visitedPackage, this::appendIssue));
    super.visitEnd();
  }

  private void appendIssue(List<Kind> kinds, String message) {
    var location = "";
    if (context.getModuleName() != null) {
      location = context.getModuleName() + "/";
    }
    location += visitedPackage.packageName() + ".package-info";

    reportBuilder.addIssue(new Issue(
        location,
        kinds,
        message
    ));
  }
}
