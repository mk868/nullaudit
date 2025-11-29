package eu.softpol.lib.nullaudit.core.type;

import java.util.List;

public final class PrimitiveTypeNode extends LeafTypeNode {

  private final char descriptor;

  public PrimitiveTypeNode(char descriptor) {
    this.descriptor = descriptor;
  }

  @Override
  public List<TypeNode> getChildren() {
    return List.of();
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

}
