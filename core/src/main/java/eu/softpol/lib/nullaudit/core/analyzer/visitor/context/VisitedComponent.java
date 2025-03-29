package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.jspecify.annotations.Nullable;

public record VisitedComponent(
    String componentName,
    String componentDescriptor,
    @Nullable String componentSignature,
    TypeNode fs
) {

}
