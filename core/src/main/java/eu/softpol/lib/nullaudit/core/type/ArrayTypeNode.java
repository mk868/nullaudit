package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import org.jspecify.annotations.Nullable;

public final class ArrayTypeNode extends TypeNode {

  private @Nullable TypeNode child;

  @Override
  public TypeNode addClassChild(String value) {
    throwWhenChildAlreadySet();
    child = new ClassTypeNode(value);
    return child;
  }

  @Override
  public TypeNode addPrimitiveChild(char descriptor) {
    throwWhenChildAlreadySet();
    child = new PrimitiveTypeNode(descriptor);
    return child;
  }

  @Override
  public TypeNode addArrayChild() {
    throwWhenChildAlreadySet();
    child = new ArrayTypeNode();
    return child;
  }

  @Override
  public TypeNode addVariableChild(String name) {
    throwWhenChildAlreadySet();
    child = new VariableTypeNode(name);
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
