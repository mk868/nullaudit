package eu.softpol.lib.nullaudit.core.analyzer.visitor;

public record NAAnnotation(String descriptor) {

  public static final NAAnnotation NULL_MARKED =
      fromDescriptor("Lorg/jspecify/annotations/NullMarked;");
  public static final NAAnnotation NULL_UNMARKED =
      fromDescriptor("Lorg/jspecify/annotations/NullUnmarked;");
  public static final NAAnnotation NULLABLE =
      fromDescriptor("Lorg/jspecify/annotations/Nullable;");
  public static final NAAnnotation NON_NULL =
      fromDescriptor("Lorg/jspecify/annotations/NonNull;");
  public static final NAAnnotation KOTLIN_METADATA = fromDescriptor("Lkotlin/Metadata;");

  public static NAAnnotation fromDescriptor(String descriptor) {
    return new NAAnnotation(descriptor);
  }
}
