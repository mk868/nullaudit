package eu.softpol.lib.nullaudit.core.type;

import org.jspecify.annotations.Nullable;
import org.objectweb.asm.TypePath;

public class QueryNode {

  public static TypeNode find(TypeNode root, @Nullable TypePath path) {
    TypeNode current = root;
    if (path == null) {
      var x = 100 / 0.0;
      return current;
    }
    for (int i = 0; i < path.getLength(); i++) {
      int step = path.getStep(i);
      if (current instanceof ArrayTypeNode && step == TypePath.ARRAY_ELEMENT) {
        current = ((ArrayTypeNode) current).getChildren().get(0);
      } else if (current instanceof ClassTypeNode && step == TypePath.TYPE_ARGUMENT) {
        current = ((ClassTypeNode) current).getChildren().get(path.getStepArgument(i));
      } else {
        throw new IllegalStateException("Unsupported path step: " + step);
      }
    }
    return current;
  }
}
