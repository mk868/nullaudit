package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class PackageInfoClassVisitor extends ClassVisitor {

  private final AnalysisContext context;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private String packageName;

  public PackageInfoClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    packageName = getPackageName(name);
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    if (descriptor.contains(Descriptors.NULL_MARKED)) {
      annotations.add(NullScopeAnnotation.NULL_MARKED);
    }
    if (descriptor.contains(Descriptors.NULL_UNMARKED)) {
      annotations.add(NullScopeAnnotation.NULL_UNMARKED);
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    var nullScope = NullScope.from(annotations);
    context.setPackageNullScope(packageName, nullScope);
    super.visitEnd();
  }
}