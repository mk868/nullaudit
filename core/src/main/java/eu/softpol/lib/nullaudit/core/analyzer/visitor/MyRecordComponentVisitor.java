package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAComponent;
import eu.softpol.lib.nullaudit.core.type.QueryNode;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyRecordComponentVisitor extends RecordComponentVisitor {

  private final NAComponent naComponent;

  protected MyRecordComponentVisitor(NAComponent naComponent) {
    super(Opcodes.ASM9);
    this.naComponent = naComponent;
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
        QueryNode.find(naComponent.fs(), typePath).addAnnotation(annotation);
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

}
