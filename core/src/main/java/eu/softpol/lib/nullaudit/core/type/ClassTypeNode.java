package eu.softpol.lib.nullaudit.core.type;

import java.util.ArrayList;
import java.util.List;

public final class ClassTypeNode extends TypeNode {

  // generic types
  private final List<TypeNode> children = new ArrayList<>();
  private final String clazz;

  public ClassTypeNode(String clazz) {
    this.clazz = clazz;
  }

  @Override
  public TypeNode addClassChild(String value) {
    var child = new ClassTypeNode(value);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addPrimitiveChild(char descriptor) {
    var child = new PrimitiveTypeNode(descriptor);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addArrayChild() {
    var child = new ArrayTypeNode();
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addVariableChild(String name) {
    var child = new VariableTypeNode(name);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addUnboundedChild() {
    var child = new UnboundedTypeNode();
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
