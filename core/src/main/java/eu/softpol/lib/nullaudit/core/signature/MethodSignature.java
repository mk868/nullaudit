package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.List;

public record MethodSignature(TypeNode returnType, List<TypeNode> parameterTypes) {

}
