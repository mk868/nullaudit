package eu.softpol.lib.nullaudit.core.analyzer;

import java.util.Set;

public enum NullScope {
  NULL_MARKED,
  NULL_UNMARKED,
  NOT_DEFINED;

  public static NullScope from(Set<NullScopeAnnotation> annotations) {
    if (annotations.contains(NullScopeAnnotation.NULL_MARKED) &&
        !annotations.contains(NullScopeAnnotation.NULL_UNMARKED)) {
      return NullScope.NULL_MARKED;
    }
    if (annotations.contains(NullScopeAnnotation.NULL_UNMARKED) &&
        !annotations.contains(NullScopeAnnotation.NULL_MARKED)) {
      return NullScope.NULL_UNMARKED;
    }
    if (annotations.contains(NullScopeAnnotation.KOTLIN_METADATA)) {
      return NullScope.NULL_UNMARKED;
    }
    return NullScope.NOT_DEFINED;
  }
}
