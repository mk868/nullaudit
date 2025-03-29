package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
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
  public void setOperator(NullnessOperator nullnessOperator) {
    //TODO not supported
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

  public String getName() {
    return name;
  }

}
