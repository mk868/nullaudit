package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface NAClass {

  default Optional<NAComponent> getComponent(String componentName) {
    return components().stream()
        .filter(c -> c.componentName().equals(componentName))
        .findFirst();
  }

  default boolean isRecord() {
    return superClazz().name().equals("java.lang.Record");
  }

  Clazz thisClazz();

  Clazz superClazz();

  Clazz topClass();

  public @Nullable Clazz outerClass();

  NullScope effectiveNullScope();

  List<NAComponent> components();

  List<NAField> fields();

  List<NAMethod> methods();

  Set<KnownAnnotations> annotations();
}
