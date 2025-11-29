package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAComponent;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNodeAnnotator;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyRecordComponentVisitor extends RecordComponentVisitor {

  private final ImmutableNAComponent.Builder naComponentBuilder;
  private TypeNode type;
  private final Runnable onEnd;

  protected MyRecordComponentVisitor(ImmutableNAComponent.Builder naComponentBuilder, TypeNode type,
      Runnable onEnd) {
    super(Opcodes.ASM9);
    this.naComponentBuilder = naComponentBuilder;
    this.type = type;
    this.onEnd = onEnd;
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naComponentBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    var annotation = TypeUseAnnotation.ofDescriptor(descriptor);
    var typeReference = new TypeReference(typeRef);
    var sort = typeReference.getSort();
    var typePathStr = typePath == null ? "" : typePath.toString();
    if (sort == TypeReference.FIELD) {
      if (typePathStr.contains("*")) {
        // TODO super not yet supported
      } else if (typePathStr.contains(".")) {
        // TODO how to handle this case...
      } else {
        this.type = TypeNodeAnnotator.annotate(this.type, typePath, annotation);
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
    naComponentBuilder.type(this.type);
    onEnd.run();
  }
}
