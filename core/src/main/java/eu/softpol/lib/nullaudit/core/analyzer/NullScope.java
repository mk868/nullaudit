package eu.softpol.lib.nullaudit.core.analyzer;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import java.util.Set;

public enum NullScope {
  NULL_MARKED,
  NULL_UNMARKED,
  NOT_DEFINED;

  public static NullScope from(Set<KnownAnnotations> annotations) {
    if (annotations.contains(KnownAnnotations.NULL_MARKED) &&
        !annotations.contains(KnownAnnotations.NULL_UNMARKED)) {
      return NullScope.NULL_MARKED;
    }
    if (annotations.contains(KnownAnnotations.NULL_UNMARKED) &&
        !annotations.contains(KnownAnnotations.NULL_MARKED)) {
      return NullScope.NULL_UNMARKED;
    }
    if (annotations.contains(KnownAnnotations.KOTLIN_METADATA)) {
      return NullScope.NULL_UNMARKED;
    }
    return NullScope.NOT_DEFINED;
  }
}
