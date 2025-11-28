package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.immutables.value.Value;
import org.jspecify.annotations.Nullable;

@Value.Immutable
public interface NAComponent {

  String componentName();

  String componentDescriptor();

  @Nullable String componentSignature();

  TypeNode type();

}
