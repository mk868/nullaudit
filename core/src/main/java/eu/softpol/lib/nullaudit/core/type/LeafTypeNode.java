package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public sealed abstract class LeafTypeNode extends TypeNode
    permits VariableTypeNode, PrimitiveTypeNode {

  protected LeafTypeNode(Builder<?> builder) {
    super(builder);
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }
}
