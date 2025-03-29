package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public abstract sealed class TypeNode permits ArrayTypeNode, PrimitiveTypeNode, ClassTypeNode,
    UnboundedTypeNode, VariableTypeNode {

  protected NullnessOperator nullnessOperator = NullnessOperator.UNSPECIFIED;

  protected TypeNode() {
  }

  protected abstract void addChild(TypeNode child);

  public TypeNode addClassChild(String value) {
    var child = new ClassTypeNode(value);
    addChild(child);
    return child;
  }

  public TypeNode addPrimitiveChild(char descriptor) {
    var child = new PrimitiveTypeNode(descriptor);
    addChild(child);
    return child;
  }

  public TypeNode addArrayChild() {
    var child = new ArrayTypeNode();
    addChild(child);
    return child;
  }

  public TypeNode addVariableChild(String name) {
    var child = new VariableTypeNode(name);
    addChild(child);
    return child;
  }

  public TypeNode addUnboundedChild() {
    var child = new UnboundedTypeNode();
    addChild(child);
    return child;
  }

  public NullnessOperator getOperator() {
    return nullnessOperator;
  }

  public void setOperator(NullnessOperator nullnessOperator) {
    this.nullnessOperator = nullnessOperator;
  }

  public abstract List<TypeNode> getChildren();
}
