package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import java.util.Arrays;
import java.util.Optional;

public enum KnownAnnotations {
  NULL_MARKED("Lorg/jspecify/annotations/NullMarked;"),
  NULL_UNMARKED("Lorg/jspecify/annotations/NullUnmarked;"),
  NULLABLE("Lorg/jspecify/annotations/Nullable;"),
  NON_NULL("Lorg/jspecify/annotations/NonNull;"),
  KOTLIN_METADATA("Lkotlin/Metadata;");

  private final String descriptor;

  KnownAnnotations(String descriptor) {
    this.descriptor = descriptor;
  }

  public static Optional<KnownAnnotations> fromDescriptor(String descriptor) {
    return Arrays.stream(KnownAnnotations.values())
        .filter(e -> e.descriptor.equals(descriptor))
        .findFirst();
  }
}
