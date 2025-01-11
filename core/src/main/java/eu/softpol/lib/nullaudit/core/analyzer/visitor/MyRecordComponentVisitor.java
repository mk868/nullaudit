package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyRecordComponentVisitor extends RecordComponentVisitor {

  private final TypeNode fs;

  protected MyRecordComponentVisitor(TypeNode fs) {
    super(Opcodes.ASM9);
    this.fs = fs;
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    if (descriptor.contains(Descriptors.NULLABLE) ||
        descriptor.contains(Descriptors.NON_NULL)
    ) {
      var operator = descriptor.contains(Descriptors.NULLABLE) ?
          NullnessOperator.UNION_NULL : NullnessOperator.MINUS_NULL;
      var typeReference = new TypeReference(typeRef);
      var sort = typeReference.getSort();
      var typePathStr = typePath == null ? "" : typePath.toString();
      if (sort == TypeReference.FIELD) {
        if (typePathStr.contains("*")) {
          // TODO super not yet supported
        } else if (typePathStr.contains(".")) {
          // TODO how to handle this case...
        } else {
          fs.toTypePath().get(typePathStr).setOperator(operator);
        }
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

}
