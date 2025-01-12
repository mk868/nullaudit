package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class UnboundedTypeNode extends TypeNode {

  UnboundedTypeNode(TypeNode parent) {
    super(parent, "?");
  }

  @Override
  public TypeNode addClassChild(String value) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addBaseChild(char descriptor) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addArrayChild() {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addVariableChild(String name) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addUnboundedChild() {
    throw createNoChildrenException();
  }

  @Override
  public void setOperator(NullnessOperator nullnessOperator) {
    throw new UnsupportedOperationException("Base type has no nullness operator");
  }

  public List<TypeNode> getChildren() {
    return List.of();
  }

  private static RuntimeException createNoChildrenException() {
    return new IllegalStateException("Wildcard type has no children");
  }
}
