package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.Map;

public record MethodSignature(TypeNode returnType, Map<Integer, TypeNode> parameterTypes) {

}
