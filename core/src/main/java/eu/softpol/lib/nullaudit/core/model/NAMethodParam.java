package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface NAMethodParam {

  Set<NAAnnotation> annotations();

  TypeNode type();
}
