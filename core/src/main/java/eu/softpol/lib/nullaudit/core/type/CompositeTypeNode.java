package eu.softpol.lib.nullaudit.core.type;

public sealed abstract class CompositeTypeNode extends TypeNode
    permits ArrayTypeNode, ClassTypeNode, UnboundedTypeNode {

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
}
