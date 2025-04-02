package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record VisitedClass(
    Clazz thisClazz,
    Clazz superClazz,
    List<VisitedComponent> components, // mutable
    List<VisitedField> fields, // mutable
    List<VisitedMethod> methods, // mutable
    Set<NullScopeAnnotation> annotations // mutable
) {

  public VisitedClass(Clazz thisClazz, Clazz superClazz) {
    this(
        thisClazz,
        superClazz,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashSet<>()
    );
  }

  public Optional<VisitedComponent> getComponent(String componentName) {
    return components.stream()
        .filter(c -> c.componentName().equals(componentName))
        .findFirst();
  }

  public boolean isRecord() {
    return superClazz.name().equals("java.lang.Record");
  }

}
