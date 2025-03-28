package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedMethod;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.translator.ToTypePathTranslator;
import java.lang.System.Logger.Level;
import java.util.HashMap;
import java.util.Map;
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

  private final Map<Integer, Map<String, TypeNode>> typePathToParameterTypes;
  private final Map<String, TypeNode> typePathToReturnType;
  private final VisitedMethod visitedMethod;

  protected MyMethodVisitor(VisitedMethod visitedMethod) {
    super(Opcodes.ASM9);
    this.visitedMethod = visitedMethod;

    var tmp = new HashMap<Integer, Map<String, TypeNode>>();
    var parameterTypes = visitedMethod.ms().parameterTypes();
    for (int i = 0; i < parameterTypes.size(); i++) {
      tmp.put(i, ToTypePathTranslator.INSTANCE.translate(parameterTypes.get(i)));
    }
    typePathToParameterTypes = Map.copyOf(tmp);
    typePathToReturnType = ToTypePathTranslator.INSTANCE.translate(visitedMethod.ms().returnType());
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    super.visitLineNumber(line, start);
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
      if (sort == TypeReference.METHOD_RETURN) {
        if (typePathStr.contains("*")) {
          // TODO super not yet supported
        } else if (typePathStr.contains(".")) {
          // TODO how to handle this case...
        } else {
          typePathToReturnType.get(typePathStr).setOperator(operator);
        }
      } else if (sort == TypeReference.METHOD_FORMAL_PARAMETER) {
        var index = typeReference.getFormalParameterIndex();
        if (typePathStr.contains("*")) {
          // TODO super not yet supported
        } else if (typePathStr.contains(".")) {
          // TODO how to handle this case...
        } else {
          typePathToParameterTypes.get(index).get(typePathStr).setOperator(operator);
        }
      } else if (sort == TypeReference.METHOD_TYPE_PARAMETER_BOUND) {
        logger.log(Level.DEBUG, "METHOD_TYPE_PARAMETER_BOUND not supported yet");
      } else {
        throw new UnsupportedOperationException("Unsupported sort: " + sort);
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    var annotation = KnownAnnotations.fromDescriptor(descriptor).orElse(null);
    if (annotation == KnownAnnotations.NULL_MARKED) {
      visitedMethod.annotations().add(NullScopeAnnotation.NULL_MARKED);
    }
    if (annotation == KnownAnnotations.NULL_UNMARKED) {
      visitedMethod.annotations().add(NullScopeAnnotation.NULL_UNMARKED);
    }
    return super.visitAnnotation(descriptor, visible);
  }
}
