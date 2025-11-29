package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class ArrayTypeNode extends CompositeTypeNode {

  private final TypeNode child;

  private ArrayTypeNode(Builder builder) {
    super(builder);
    this.child = Objects.requireNonNull(builder.child, "Array must have a component type");
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of(child);
  }

  @Override
  public Builder toBuilder() {
    return new Builder()
        .withComponentType(child)
        .addAnnotations(getAnnotations());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    private @Nullable TypeNode child;

    public Builder withComponentType(TypeNode child) {
      this.child = child;
      return this;
    }

    @Override
    public ArrayTypeNode build() {
      if (this.child == null) {
        throw new IllegalStateException("Cannot build ArrayTypeNode without a component type");
      }
      return new ArrayTypeNode(this);
    }
  }
}
