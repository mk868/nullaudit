package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import java.util.List;
import java.util.Set;

public record VisitedClass(
    List<VisitedMethod> methods, // mutable
    Set<NullScopeAnnotation> annotations // mutable
) {

}
