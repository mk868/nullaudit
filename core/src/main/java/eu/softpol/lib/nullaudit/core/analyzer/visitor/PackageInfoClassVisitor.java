package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.model.MutableNAPackage;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAPackage;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class PackageInfoClassVisitor extends ClassVisitor {

  private final AnalysisContext context;
  private MutableNAPackage naPackage;

  public PackageInfoClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    naPackage = new MutableNAPackage(
        getPackageName(name)
    );
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naPackage.addAnnotation(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    var nullScope = NullScope.from(naPackage.annotations());
    context.setPackageNullScope(naPackage.packageName(), nullScope);
    super.visitEnd();
  }

  public NAPackage getNaPackage() {
    return naPackage;
  }
}
