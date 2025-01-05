package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.lang.System.Logger.Level;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
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
  private final MethodInfo methodInfo;

  protected MyMethodVisitor(MethodInfo methodInfo) {
    super(Opcodes.ASM9);
    this.methodInfo = methodInfo;

    typePathToParameterTypes = methodInfo.ms().parameterTypes().entrySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Entry::getKey,
            kv -> kv.getValue().toTypePath()
        ));
    typePathToReturnType = methodInfo.ms().returnType().toTypePath();
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    super.visitLineNumber(line, start);
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
        logger.log(Level.WARNING, "METHOD_TYPE_PARAMETER_BOUND not supported yet");
      } else {
        throw new UnsupportedOperationException("Unsupported sort: " + sort);
      }
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    if (descriptor.contains(Descriptors.NULL_MARKED)) {
      methodInfo.annotations().add(NullScopeAnnotation.NULL_MARKED);
    }
    if (descriptor.contains(Descriptors.NULL_UNMARKED)) {
      methodInfo.annotations().add(NullScopeAnnotation.NULL_UNMARKED);
    }
    return super.visitAnnotation(descriptor, visible);
  }
}
