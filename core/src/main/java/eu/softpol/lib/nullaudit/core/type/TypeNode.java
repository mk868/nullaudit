package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;
import org.jspecify.annotations.Nullable;

public abstract sealed class TypeNode permits ArrayTypeNode, PrimitiveTypeNode, ClassTypeNode,
    UnboundedTypeNode, VariableTypeNode {

  protected final @Nullable TypeNode parent;
  protected NullnessOperator nullnessOperator = NullnessOperator.UNSPECIFIED;

  protected TypeNode(@Nullable TypeNode parent) {
    this.parent = parent;
  }

  protected TypeNode() {
    this(null);
  }

  public abstract TypeNode addClassChild(String value);

  public abstract TypeNode addPrimitiveChild(char descriptor);

  public abstract TypeNode addArrayChild();

  public abstract TypeNode addVariableChild(String name);

  public abstract TypeNode addUnboundedChild();

  public NullnessOperator getOperator() {
    return nullnessOperator;
  }

  public void setOperator(NullnessOperator nullnessOperator) {
    this.nullnessOperator = nullnessOperator;
  }

  public abstract List<TypeNode> getChildren();

  public @Nullable TypeNode getParent() {
    return parent;
  }
}
