package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAField;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNodeAnnotator;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyFieldVisitor extends FieldVisitor {

  private final ImmutableNAField.Builder naFieldBuilder;
  private TypeNode type;
  private final Runnable onEnd;

  protected MyFieldVisitor(ImmutableNAField.Builder naFieldBuilder, TypeNode type, Runnable onEnd) {
    super(Opcodes.ASM9);
    this.naFieldBuilder = naFieldBuilder;
    this.type = type;
    this.onEnd = onEnd;
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naFieldBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    var annotation = TypeUseAnnotation.ofDescriptor(descriptor);
    var typeReference = new TypeReference(typeRef);
    var sort = typeReference.getSort();
    if (sort == TypeReference.FIELD) {
      this.type = TypeNodeAnnotator.annotate(this.type, typePath, annotation);
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
    naFieldBuilder.type(this.type);
    onEnd.run();
  }
}
