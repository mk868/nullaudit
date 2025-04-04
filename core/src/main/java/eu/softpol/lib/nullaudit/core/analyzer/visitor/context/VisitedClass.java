package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VisitedClass {

  default Optional<VisitedComponent> getComponent(String componentName) {
    return components().stream()
        .filter(c -> c.componentName().equals(componentName))
        .findFirst();
  }

  default boolean isRecord() {
    return superClazz().name().equals("java.lang.Record");
  }

  Clazz thisClazz();

  Clazz superClazz();

  NullScope effectiveNullScope();

  List<VisitedComponent> components();

  List<VisitedField> fields();

  List<VisitedMethod> methods();

  Set<NullScopeAnnotation> annotations();
}
