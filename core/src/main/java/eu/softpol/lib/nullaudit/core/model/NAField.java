package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record NAField(
    String fieldName,
    String fieldDescriptor,
    @Nullable String fieldSignature,
    TypeNode fs
) {

}
