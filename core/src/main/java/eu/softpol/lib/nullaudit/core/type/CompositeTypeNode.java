package eu.softpol.lib.nullaudit.core.type;

public sealed abstract class CompositeTypeNode extends TypeNode
    permits ArrayTypeNode, ClassTypeNode, UnboundedTypeNode {

  protected CompositeTypeNode(Builder<?> builder) {
    super(builder);
  }
}
