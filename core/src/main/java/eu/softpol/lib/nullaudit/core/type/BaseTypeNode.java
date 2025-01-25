package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public final class BaseTypeNode extends TypeNode {

  private final char descriptor;

  BaseTypeNode(TypeNode parent, char descriptor) {
    super(parent);
    this.descriptor = descriptor;
  }

  public BaseTypeNode(char descriptor) {
    super();
    this.descriptor = descriptor;
  }

  @Override
  public TypeNode addClassChild(String value) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addBaseChild(char descriptor) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addArrayChild() {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addVariableChild(String name) {
    throw createNoChildrenException();
  }

  @Override
  public TypeNode addUnboundedChild() {
    throw createNoChildrenException();
  }

  @Override
  public void setOperator(NullnessOperator nullnessOperator) {
    throw new UnsupportedOperationException("Base type has no nullness operator");
  }

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

  private static RuntimeException createNoChildrenException() {
    return new IllegalStateException("Base type has no children");
  }
}
