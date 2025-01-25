package eu.softpol.lib.nullaudit.core.type.translator;

import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.HashMap;
import java.util.Map;

public class ToTypePathTranslator implements Translator<Map<String, TypeNode>> {

  public static final ToTypePathTranslator INSTANCE = new ToTypePathTranslator();

  @Override
  public Map<String, TypeNode> translate(TypeNode typeNode) {
    var result = new HashMap<String, TypeNode>();
    toTypePath(typeNode, "", result);
    return Map.copyOf(result);
  }

  private static void toTypePath(TypeNode node, String prefix, Map<String, TypeNode> out) {
    out.put(prefix, node);
    for (int i = 0; i < node.getChildren().size(); i++) {
      var child = node.getChildren().get(i);
      final String newPrefix;
      if (node instanceof ArrayTypeNode) {
        newPrefix = prefix + "[";
      } else {
        newPrefix = prefix + i + ";";
      }
      toTypePath(child, newPrefix, out);
    }
  }
}
