package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedField;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedMethod;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.translator.ToTypePathTranslator;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

public class MyFieldVisitor extends FieldVisitor {

  private final VisitedField visitedField;

  protected MyFieldVisitor(VisitedField visitedField) {
    super(Opcodes.ASM9);
    this.visitedField = visitedField;
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    var annotation = KnownAnnotations.fromDescriptor(descriptor).orElse(null);
    if (annotation == KnownAnnotations.NULLABLE ||
        annotation == KnownAnnotations.NON_NULL
    ) {
      var operator = annotation == KnownAnnotations.NULLABLE ?
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
          ToTypePathTranslator.INSTANCE.translate(visitedField.fs()).get(typePathStr).setOperator(operator);
        }
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

}
