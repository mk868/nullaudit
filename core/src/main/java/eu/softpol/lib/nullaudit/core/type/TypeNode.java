package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import java.util.List;

public abstract sealed class TypeNode permits ArrayTypeNode, PrimitiveTypeNode, ClassTypeNode,
    UnboundedTypeNode, VariableTypeNode {

  protected NullnessOperator nullnessOperator = NullnessOperator.UNSPECIFIED;

  protected TypeNode() {
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
}
