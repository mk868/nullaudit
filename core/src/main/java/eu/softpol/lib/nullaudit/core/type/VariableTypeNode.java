package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public final class VariableTypeNode extends LeafTypeNode {

  private final String name;

  public VariableTypeNode(String name) {
    this.name = name;
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

  public String getName() {
    return name;
  }

}
