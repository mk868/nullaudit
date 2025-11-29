package eu.softpol.lib.nullaudit.core.type;

import java.util.ArrayList;
import java.util.List;

public final class ClassTypeNode extends CompositeTypeNode {

  // generic types
  private final List<TypeNode> children;
  private final String clazz;

  private ClassTypeNode(Builder builder) {
    super(builder);
    this.clazz = builder.clazz;
    this.children = List.copyOf(builder.children);
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.copyOf(children);
  }

  public String getClazz() {
    return clazz;
  }

  @Override
  public Builder toBuilder() {
    var builder = new Builder(clazz)
        .addAnnotations(getAnnotations());
    children.forEach(builder::addChild);
    return builder;
  }

  public static Builder builder(String clazz) {
    return new Builder(clazz);
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    private final String clazz;
    private final List<TypeNode> children = new ArrayList<>();

    private Builder(String clazz) {
      this.clazz = clazz;
    }

    public Builder addChild(TypeNode child) {
      this.children.add(child);
      return this;
    }

    public Builder clearChildren() {
      this.children.clear();
      return this;
    }

    @Override
    public ClassTypeNode build() {
      return new ClassTypeNode(this);
    }
  }
}
