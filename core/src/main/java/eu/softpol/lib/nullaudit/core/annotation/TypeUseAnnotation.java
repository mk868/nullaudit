package eu.softpol.lib.nullaudit.core.annotation;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil;

public record TypeUseAnnotation(String clazz) {

  public static final TypeUseAnnotation JSPECIFY_NULLABLE =
      ofClazz("org.jspecify.annotations.Nullable");
  public static final TypeUseAnnotation JSPECIFY_NON_NULL =
      ofClazz("org.jspecify.annotations.NonNull");

  public static TypeUseAnnotation ofDescriptor(String descriptor) {
    return ofClazz(ClassUtil.getClassNameFromDescriptor(descriptor));
  }

  public static TypeUseAnnotation ofClazz(String clazz) {
    return new TypeUseAnnotation(clazz);
  }
}
