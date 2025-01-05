package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.AnalysisContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

public class ModuleInfoClassVisitor extends ClassVisitor {

  private final AnalysisContext context;

  public ModuleInfoClassVisitor(AnalysisContext context) {
    super(Opcodes.ASM9);
    this.context = context;
  }

  @Override
  public ModuleVisitor visitModule(String name, int access, String version) {
    context.setModuleName(name);
    return super.visitModule(name, access, version);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    if (descriptor.contains(Descriptors.NULL_MARKED)) {
      context.setModuleInfoNullMarked(true);
    }
    return super.visitAnnotation(descriptor, visible);
  }
}
