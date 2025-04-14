package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record NAField(
    String fieldName,
    String fieldDescriptor,
    @Nullable String fieldSignature,
    TypeNode fs
) {

}
