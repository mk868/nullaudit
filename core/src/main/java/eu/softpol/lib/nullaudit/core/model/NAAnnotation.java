package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil;

public record NAAnnotation(String fqcn) {

  public static final NAAnnotation NULL_MARKED = of("org.jspecify.annotations.NullMarked");
  public static final NAAnnotation NULL_UNMARKED = of("org.jspecify.annotations.NullUnmarked");
  public static final NAAnnotation NULLABLE = of("org.jspecify.annotations.Nullable");
  public static final NAAnnotation NON_NULL = of("org.jspecify.annotations.NonNull");
  public static final NAAnnotation KOTLIN_METADATA = of("kotlin.Metadata");

  public static NAAnnotation of(String fqcn) {
    return new NAAnnotation(fqcn);
  }

  public static NAAnnotation fromDescriptor(String descriptor) {
    return new NAAnnotation(ClassUtil.getClassNameFromDescriptor(descriptor));
  }
}
