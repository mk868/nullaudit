package eu.softpol.lib.nullaudit.core.type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ClassTypeNode extends TypeNode {

  // generic types
  private final List<TypeNode> children = new ArrayList<>();

  ClassTypeNode(TypeNode parent, String clazz) {
    super(parent, clazz);
  }

  public ClassTypeNode(String clazz) {
    super(clazz);
  }

  @Override
  public TypeNode addClassChild(String value) {
    var child = new ClassTypeNode(this, value);
    children.add(child);
    return child;
  }

  @Override
  public TypeNode addBaseChild(char descriptor) {
    var child = new BaseTypeNode(this, descriptor);
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

  @Override
  public String toString() {
    var result = value + nullnessOperatorToString();
    if (!children.isEmpty()) {
      result += "<" + children.stream()
          .map(TypeNode::toString)
          .collect(Collectors.joining(", "))
                + ">";
    }
    return result;
  }
}
