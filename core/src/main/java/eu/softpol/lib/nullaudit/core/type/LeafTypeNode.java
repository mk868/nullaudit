package eu.softpol.lib.nullaudit.core.type;

public sealed abstract class LeafTypeNode extends TypeNode
    permits VariableTypeNode, PrimitiveTypeNode {

}
