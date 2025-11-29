package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class WildcardTypeNode extends CompositeTypeNode {

  public enum Bound {
    EXTENDS, // +
    SUPER    // -
  }

  private final Bound bound;
  private final TypeNode boundType;

  private WildcardTypeNode(Builder builder) {
    super(builder);
    this.bound = Objects.requireNonNull(builder.bound, "Bound must be set");
    this.boundType = Objects.requireNonNull(builder.boundType, "Bound type must be set");
  }

  public Bound getBound() {
    return bound;
  }

  public TypeNode getBoundType() {
    return boundType;
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of(boundType);
  }

  @Override
  public Builder toBuilder() {
    return new Builder()
        .bound(bound)
        .boundType(boundType)
        .addAnnotations(getAnnotations());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    private @Nullable Bound bound;
    private @Nullable TypeNode boundType;

    public Builder bound(Bound bound) {
      this.bound = bound;
      return this;
    }

    public Builder boundType(TypeNode boundType) {
      this.boundType = boundType;
      return this;
    }

    @Override
    public WildcardTypeNode build() {
      if (bound == null || boundType == null) {
        throw new IllegalStateException("WildcardTypeNode requires bound and boundType");
      }
      return new WildcardTypeNode(this);
    }
  }
}
