package eu.softpol.lib.nullaudit.core.type;

import java.util.List;
import org.jspecify.annotations.Nullable;

public final class ArrayTypeNode extends TypeNode {

  private @Nullable TypeNode child;

  @Override
  protected void addChild(TypeNode child) {
    throwWhenChildAlreadySet();
    this.child = child;
  }

  @Override
  public TypeNode addUnboundedChild() {
    throw new UnsupportedOperationException("Array type cannot have unbounded child");
  }

  @Override
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
