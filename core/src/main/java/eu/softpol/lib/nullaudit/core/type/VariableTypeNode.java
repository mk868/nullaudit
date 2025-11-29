package eu.softpol.lib.nullaudit.core.type;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class VariableTypeNode extends LeafTypeNode {

  private final String name;

  private VariableTypeNode(Builder builder) {
    super(builder);
    this.name = Objects.requireNonNull(builder.name, "Variable must have a name");
  }

  public String getName() {
    return name;
  }

  @Override
  public Builder toBuilder() {
    return new Builder()
        .name(name)
        .addAnnotations(getAnnotations());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    private @Nullable String name;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    @Override
    public VariableTypeNode build() {
      if (this.name == null) {
        throw new IllegalStateException("Cannot build VariableTypeNode without a name");
      }
      return new VariableTypeNode(this);
    }
  }

}
