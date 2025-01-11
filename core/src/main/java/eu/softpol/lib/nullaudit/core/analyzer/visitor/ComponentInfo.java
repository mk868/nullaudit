package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record ComponentInfo(
    String componentName,
    String componentDescriptor,
    @Nullable String componentSignature,
    TypeNode fs
) {

}
