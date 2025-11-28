package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.immutables.value.Value;

@Value.Immutable
public interface NAMethodParam {

  TypeNode type();
}
