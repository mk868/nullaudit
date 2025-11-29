package eu.softpol.lib.nullaudit.core.type;

import java.util.ArrayList;
import java.util.List;

public final class ClassTypeNode extends CompositeTypeNode {

  // generic types
  private final List<TypeNode> children = new ArrayList<>();
  private final String clazz;

  public ClassTypeNode(String clazz) {
    this.clazz = clazz;
  }

  @Override
  protected void addChild(TypeNode child) {
    children.add(child);
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.copyOf(children);
  }

  public String getClazz() {
    return clazz;
  }
}
