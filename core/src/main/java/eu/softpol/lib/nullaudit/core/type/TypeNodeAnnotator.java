package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.TypePath;

public class TypeNodeAnnotator {

  public static TypeNode annotate(TypeNode root, @Nullable TypePath typePath,
      NAAnnotation annotation) {
    if (typePath == null) {
      return root.toBuilder().addAnnotation(annotation).build();
    }
    return traverse(root, typePath, 0, annotation);
  }

  private static TypeNode traverse(TypeNode node, TypePath path, int pathIndex,
      NAAnnotation annotation) {
    if (pathIndex >= path.getLength()) {
      return node.toBuilder().addAnnotation(annotation).build();
    }

    int step = path.getStep(pathIndex);
    int argumentIndex = path.getStepArgument(pathIndex);

    if (step == TypePath.ARRAY_ELEMENT) {
      if (node instanceof ArrayTypeNode arrayNode) {
        TypeNode newChild = traverse(arrayNode.getChildren().get(0), path, pathIndex + 1,
            annotation);
        return arrayNode.toBuilder()
            .withComponentType(newChild)
            .build();
      }
      throw new IllegalStateException(
          "TypePath expects ARRAY_ELEMENT but found " + node.getClass().getSimpleName());

    } else if (step == TypePath.TYPE_ARGUMENT) {
      if (node instanceof ClassTypeNode classNode) {
        List<TypeNode> children = new ArrayList<>(classNode.getChildren());
        if (argumentIndex >= children.size()) {
          throw new IllegalStateException("Invalid type argument index: " + argumentIndex);
        }

        TypeNode modifiedChild = traverse(children.get(argumentIndex), path, pathIndex + 1,
            annotation);
        children.set(argumentIndex, modifiedChild);

        var builder = classNode.toBuilder();
        builder.clearChildren();
        children.forEach(builder::addChild);
        return builder.build();
      }
    } else if (step == TypePath.WILDCARD_BOUND) {
      if (node instanceof WildcardTypeNode wildcardNode) {
        TypeNode newBound = traverse(wildcardNode.getBoundType(), path, pathIndex + 1, annotation);
        return wildcardNode.toBuilder()
            .boundType(newBound)
            .build();
      }
      throw new IllegalStateException(
          "TypePath expects WILDCARD_BOUND but found " + node.getClass().getSimpleName());
    }

    return node;
  }
}