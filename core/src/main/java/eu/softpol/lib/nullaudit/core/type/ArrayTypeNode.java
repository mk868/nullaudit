package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class ArrayTypeNode extends TypeNode {

  private @Nullable TypeNode child;

  ArrayTypeNode(TypeNode parent) {
    super(parent, "[]");
  }

  public ArrayTypeNode() {
    super("[]");
  }

  @Override
  public TypeNode addClassChild(String value) {
    throwWhenChildAlreadySet();
    child = new ClassTypeNode(this, value);
    return child;
  }

  @Override
  public TypeNode addBaseChild(char descriptor) {
    throwWhenChildAlreadySet();
    child = new BaseTypeNode(this, descriptor);
    return child;
  }

  @Override
  public TypeNode addArrayChild() {
    throwWhenChildAlreadySet();
    child = new ArrayTypeNode(this);
    return child;
  }

  @Override
  public TypeNode addVariableChild(String name) {
    throwWhenChildAlreadySet();
    child = new VariableTypeNode(this, name);
    return child;
  }

  public List<TypeNode> getChildren() {
    if (child == null) {
      throw new IllegalStateException("Array type has no children");
    }
    return List.of(child);
  }

  @Override
  public String toString() {
    return Objects.requireNonNull(child) + "[]" + nullnessOperatorToString();
  }

  private void throwWhenChildAlreadySet() {
    if (child != null) {
      throw new IllegalStateException("Array type already has a child");
    }
  }
}