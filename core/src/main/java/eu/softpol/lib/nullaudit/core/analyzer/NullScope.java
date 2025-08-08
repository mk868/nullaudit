package eu.softpol.lib.nullaudit.core.analyzer;

import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import java.util.Set;

public enum NullScope {
  NULL_MARKED,
  NULL_UNMARKED,
  NOT_DEFINED;

  public static NullScope from(Set<NAAnnotation> annotations) {
    if (annotations.contains(NAAnnotation.NULL_MARKED) &&
        !annotations.contains(NAAnnotation.NULL_UNMARKED)) {
      return NullScope.NULL_MARKED;
    }
    if (annotations.contains(NAAnnotation.NULL_UNMARKED) &&
        !annotations.contains(NAAnnotation.NULL_MARKED)) {
      return NullScope.NULL_UNMARKED;
    }
    if (annotations.contains(NAAnnotation.KOTLIN_METADATA)) {
      return NullScope.NULL_UNMARKED;
    }
    return NullScope.NOT_DEFINED;
  }
}
