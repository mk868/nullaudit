package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.MutableNAMethod;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.type.QueryNode;
import java.lang.System.Logger.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

/**
 * Appends nullness info to the type signature
 */
public class MyMethodVisitor extends MethodVisitor {

  private static final System.Logger logger = System.getLogger(MyMethodVisitor.class.getName());

  private final MutableNAMethod naMethod;

  protected MyMethodVisitor(MutableNAMethod naMethod) {
    super(Opcodes.ASM9);
    this.naMethod = naMethod;
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    super.visitLineNumber(line, start);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    var annotation = TypeUseAnnotation.ofDescriptor(descriptor);
    var typeReference = new TypeReference(typeRef);
    var sort = typeReference.getSort();
    var typePathStr = typePath == null ? "" : typePath.toString();
    if (sort == TypeReference.METHOD_RETURN) {
      if (typePathStr.contains("*")) {
        // TODO super not yet supported
      } else if (typePathStr.contains(".")) {
        // TODO how to handle this case...
      } else {
        QueryNode.find(naMethod.ms().returnType(), typePath).addAnnotation(annotation);
      }
    } else if (sort == TypeReference.METHOD_FORMAL_PARAMETER) {
      var index = typeReference.getFormalParameterIndex();
      if (typePathStr.contains("*")) {
        // TODO super not yet supported
      } else if (typePathStr.contains(".")) {
        // TODO how to handle this case...
      } else {
        QueryNode.find(naMethod.ms().parameterTypes().get(index), typePath)
            .addAnnotation(annotation);
      }
    } else if (sort == TypeReference.METHOD_TYPE_PARAMETER_BOUND) {
      logger.log(Level.DEBUG, "METHOD_TYPE_PARAMETER_BOUND not supported yet");
    } else if (sort == TypeReference.METHOD_TYPE_PARAMETER) {
      logger.log(Level.DEBUG, "METHOD_TYPE_PARAMETER not supported yet");
    } else {
      throw new UnsupportedOperationException("Unsupported sort: " + sort);
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    KnownAnnotations.fromDescriptor(descriptor)
        .ifPresent(naMethod::addAnnotation);
    return super.visitAnnotation(descriptor, visible);
  }
}
