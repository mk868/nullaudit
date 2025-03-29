package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class VariableTypeNode extends TypeNode {

  private final String name;

  VariableTypeNode(TypeNode parent, String name) {
    super(parent);
    this.name = name;
  }

  public VariableTypeNode(String name) {
    super();
    this.name = name;
  }

  @Override
  public TypeNode addClassChild(String value) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addPrimitiveChild(char descriptor) {
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

  public String getName() {
    return name;
  }

  private static RuntimeException createNoChildrenException() {
    return new IllegalStateException("Variable type has no children");
  }
}
