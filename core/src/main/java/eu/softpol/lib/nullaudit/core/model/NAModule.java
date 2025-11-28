package eu.softpol.lib.nullaudit.core.model;

import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface NAModule {

  String moduleName();

  Set<NAAnnotation> annotations();
}
