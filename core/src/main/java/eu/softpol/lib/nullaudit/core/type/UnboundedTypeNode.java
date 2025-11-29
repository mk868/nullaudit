package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public final class UnboundedTypeNode extends CompositeTypeNode {

  private UnboundedTypeNode(Builder builder) {
    super(builder);
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
  }

  @Override
  public Builder toBuilder() {
    return new Builder()
        .addAnnotations(getAnnotations());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    @Override
    public UnboundedTypeNode build() {
      return new UnboundedTypeNode(this);
    }
  }
}
