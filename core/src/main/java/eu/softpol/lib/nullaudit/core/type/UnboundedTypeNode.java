package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public final class UnboundedTypeNode extends TypeNode {

  @Override
  protected void addChild(TypeNode child) {
    throw new IllegalStateException("Wildcard type has no children");
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

}
