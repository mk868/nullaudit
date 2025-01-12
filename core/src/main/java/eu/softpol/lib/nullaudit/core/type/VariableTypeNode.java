package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class VariableTypeNode extends TypeNode {

  VariableTypeNode(TypeNode parent, String name) {
    super(parent, name);
  }

  public VariableTypeNode(String name) {
    super(name);
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
    //TODO not supported
  }

  public List<TypeNode> getChildren() {
    return List.of();
  }

  private static RuntimeException createNoChildrenException() {
    return new IllegalStateException("Variable type has no children");
  }
}
