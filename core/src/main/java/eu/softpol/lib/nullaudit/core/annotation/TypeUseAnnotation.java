package eu.softpol.lib.nullaudit.core.annotation;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;

public record TypeUseAnnotation(String fqcn) {

  public static final TypeUseAnnotation JSPECIFY_NULLABLE =
      of(NAAnnotation.NULLABLE.fqcn());
  public static final TypeUseAnnotation JSPECIFY_NON_NULL =
      of(NAAnnotation.NON_NULL.fqcn());

  public static TypeUseAnnotation ofDescriptor(String descriptor) {
    return of(ClassUtil.getClassNameFromDescriptor(descriptor));
  }

  public static TypeUseAnnotation of(String fqcn) {
    return new TypeUseAnnotation(fqcn);
  }
}
