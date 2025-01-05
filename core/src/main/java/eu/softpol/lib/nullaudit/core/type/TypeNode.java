package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public abstract sealed class TypeNode permits ArrayTypeNode, BaseTypeNode, ClassTypeNode,
    VariableTypeNode {

  protected final @Nullable TypeNode parent;
  protected final String value;
  protected NullnessOperator nullnessOperator = NullnessOperator.UNSPECIFIED;

  protected TypeNode(@Nullable TypeNode parent, String value) {
    this.parent = parent;
    this.value = value;
  }

  protected TypeNode(String value) {
    this(null, value);
  }

  public abstract TypeNode addClassChild(String value);

  public abstract TypeNode addBaseChild(char descriptor);

  public abstract TypeNode addArrayChild();

  public abstract TypeNode addVariableChild(String name);

  public String getValue() {
    return value;
  }

  public NullnessOperator getOperator() {
    return nullnessOperator;
  }

  public void setOperator(NullnessOperator nullnessOperator) {
    this.nullnessOperator = nullnessOperator;
  }

  public abstract List<TypeNode> getChildren();

  public @Nullable TypeNode getParent() {
    return parent;
  }

  @Override
  public String toString() {
    return value;
  }

  public Map<String, TypeNode> toTypePath() {
    var result = new HashMap<String, TypeNode>();
    toTypePath(this, "", result);
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

  protected String nullnessOperatorToString() {
    return switch (nullnessOperator) {
      case UNION_NULL -> "?";
      case MINUS_NULL -> "!";
      default -> "*";
    };
  }
}
