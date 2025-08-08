package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record NAComponent(
    String componentName,
    String componentDescriptor,
    @Nullable String componentSignature,
    TypeNode fs
) {

}
