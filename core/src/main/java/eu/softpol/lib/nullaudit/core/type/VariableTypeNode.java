package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public final class VariableTypeNode extends TypeNode {

  private final String name;

  public VariableTypeNode(String name) {
    this.name = name;
  }

  @Override
  protected void addChild(TypeNode child) {
    throw new IllegalStateException("Variable type has no children");
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

  public String getName() {
    return name;
  }

}
