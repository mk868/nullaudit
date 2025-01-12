package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record FieldInfo(
    String fieldName,
    String fieldDescriptor,
    @Nullable String fieldSignature,
    TypeNode fs
) {

}
