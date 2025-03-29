package eu.softpol.lib.nullaudit.core.type;

import java.util.ArrayList;
import java.util.List;

public final class ClassTypeNode extends TypeNode {

  // generic types
  private final List<TypeNode> children = new ArrayList<>();
  private final String clazz;

  ClassTypeNode(TypeNode parent, String clazz) {
    super(parent);
    this.clazz = clazz;
  }

  public ClassTypeNode(String clazz) {
    super();
    this.clazz = clazz;
  }

  @Override
  public TypeNode addClassChild(String value) {
    var child = new ClassTypeNode(this, value);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addPrimitiveChild(char descriptor) {
    var child = new PrimitiveTypeNode(this, descriptor);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addArrayChild() {
    var child = new ArrayTypeNode(this);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addVariableChild(String name) {
    var child = new VariableTypeNode(this, name);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addUnboundedChild() {
    var child = new UnboundedTypeNode(this);
    children.add(child);
    return child;
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.copyOf(children);
  }

  public String getClazz() {
    return clazz;
  }
}
