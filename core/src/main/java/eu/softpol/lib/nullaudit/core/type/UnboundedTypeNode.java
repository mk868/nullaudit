package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class UnboundedTypeNode extends TypeNode {

  @Override
  protected void addChild(TypeNode child) {
    throw new IllegalStateException("Wildcard type has no children");
  }

  @Override
  public void setOperator(NullnessOperator nullnessOperator) {
    throw new UnsupportedOperationException("Base type has no nullness operator");
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

}
