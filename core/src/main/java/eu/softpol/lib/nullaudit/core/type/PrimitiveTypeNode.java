package eu.softpol.lib.nullaudit.core.type;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class PrimitiveTypeNode extends LeafTypeNode {

  private final char descriptor;

  private PrimitiveTypeNode(Builder builder) {
    super(builder);
    this.descriptor = Objects.requireNonNull(builder.descriptor, "Descriptor must be set");
  }

  public char getDescriptor() {
    return descriptor;
  }

  public String getName() {
    return switch (descriptor) {
      case 'B' -> "byte";
      case 'C' -> "char";
      case 'D' -> "double";
      case 'F' -> "float";
      case 'I' -> "int";
      case 'J' -> "long";
      case 'S' -> "short";
      case 'Z' -> "boolean";
      case 'V' -> "void";
      default -> throw new IllegalStateException("Unsupported descriptor: " + descriptor);
    };
  }

  @Override
  public Builder toBuilder() {
    return new Builder()
        .descriptor(descriptor)
        .addAnnotations(getAnnotations());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends TypeNode.Builder<Builder> {

    private @Nullable Character descriptor;

    public Builder descriptor(char descriptor) {
      this.descriptor = descriptor;
      return this;
    }

    @Override
    public PrimitiveTypeNode build() {
      if (this.descriptor == null) {
        throw new IllegalStateException("Cannot build PrimitiveTypeNode without a descriptor");
      }
      return new PrimitiveTypeNode(this);
    }
  }

}
