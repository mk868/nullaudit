package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.Set;
import org.immutables.value.Value;
import org.jspecify.annotations.Nullable;

@Value.Immutable
public interface NAField {

  String fieldName();

  String fieldDescriptor();

  @Nullable String fieldSignature();

  TypeNode type();

  Set<NAAnnotation> annotations();

}
