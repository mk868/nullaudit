package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class PrimitiveTypeNode extends TypeNode {

  private final char descriptor;

  public PrimitiveTypeNode(char descriptor) {
    this.descriptor = descriptor;
  }

  @Override
  protected void addChild(TypeNode child) {
    throw new IllegalStateException("Base type has no children");
  }

  @Override
  public void setOperator(NullnessOperator nullnessOperator) {
    throw new UnsupportedOperationException("Base type has no nullness operator");
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
