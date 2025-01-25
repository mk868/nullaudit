package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import org.jspecify.annotations.Nullable;

public final class ArrayTypeNode extends TypeNode {

  private @Nullable TypeNode child;

  ArrayTypeNode(TypeNode parent) {
    super(parent);
  }

  public ArrayTypeNode() {
    super();
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

  @Override
  public TypeNode addUnboundedChild() {
    throw new UnsupportedOperationException("Array type cannot have unbounded child");
  }

  public List<TypeNode> getChildren() {
    if (child == null) {
      throw new IllegalStateException("Array type has no children");
    }
    return List.of(child);
  }

  private void throwWhenChildAlreadySet() {
    if (child != null) {
      throw new IllegalStateException("Array type already has a child");
    }
  }
}
