package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.NAField;
import eu.softpol.lib.nullaudit.core.type.QueryNode;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyFieldVisitor extends FieldVisitor {

  private final NAField naField;

  protected MyFieldVisitor(NAField naField) {
    super(Opcodes.ASM9);
    this.naField = naField;
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
        QueryNode.find(naField.fs(), typePath).addAnnotation(annotation);
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

}
