package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAPackage;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAPackage;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class PackageInfoClassVisitor extends ClassVisitor {

  private final ImmutableNAPackage.Builder naPackageBuilder = ImmutableNAPackage.builder();
  private final AnalysisContext context;
  private @Nullable NAPackage naPackage;

  public PackageInfoClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    naPackageBuilder.packageName(getPackageName(name));
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naPackageBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    naPackage = naPackageBuilder.build();
    var nullScope = NullScope.from(naPackage.annotations());
    context.setPackageNullScope(naPackage.packageName(), nullScope);
    super.visitEnd();
  }

  public NAPackage getNaPackage() {
    return Objects.requireNonNull(naPackage, "naPackage");
  }
}
