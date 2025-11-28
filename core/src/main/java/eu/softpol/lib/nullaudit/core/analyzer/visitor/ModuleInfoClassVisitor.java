package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.model.ImmutableNAModule;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAModule;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

public class ModuleInfoClassVisitor extends ClassVisitor {

  private final ImmutableNAModule.Builder builder;
  private @Nullable NAModule naModule;

  public ModuleInfoClassVisitor() {
    super(Opcodes.ASM9);
    this.builder = ImmutableNAModule.builder();
  }

  @Override
  public ModuleVisitor visitModule(String name, int access, String version) {
    builder.moduleName(name);
    return super.visitModule(name, access, version);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    builder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
    naModule = builder.build();
  }

  public NAModule getNAModule() {
    return Objects.requireNonNull(naModule, "naModule");
  }
}
