package eu.softpol.lib.nullaudit.core.check.prohibit_non_jspecify_annotations;

import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import java.util.Arrays;
import java.util.List;

public enum ProhibitedAnnotations {
  // jakarta
  JAKARTA_NULLABLE("jakarta.annotation.Nullable"),
  JAKARTA_NONNULL("jakarta.annotation.Nonnull"),
  // JSR-305
  JAVAX_NULLABLE("javax.annotation.Nullable"),
  JAVAX_NONNULL("javax.annotation.Nonnull"),
  // Eclipse JDT
  JDT_NULLABLE("org.eclipse.jdt.annotation.Nullable"),
  JDT_NONNULL("org.eclipse.jdt.annotation.NonNull"),
  // TODO org.eclipse.jdt.annotation.NonNullByDefault
  // Checker Framework
  CHECKER_FRAMEWORK_NULLABLE("org.checkerframework.checker.nullness.qual.Nullable"),
  CHECKER_FRAMEWORK_NON_NULL("org.checkerframework.checker.nullness.qual.NonNull"),
  // TODO org.checkerframework.framework.qual.DefaultQualifier(NonNull.class)
  // TODO org.checkerframework.framework.qual.DefaultQualifier(Nullable.class)
  // JetBrains
  JETBRAINS_NULLABLE("org.jetbrains.annotations.Nullable"),
  JETBRAINS_NOT_NULL("org.jetbrains.annotations.NotNull"),
  // Spotbugs
  SPOTBUGS_NULLABLE("edu.umd.cs.findbugs.annotations.Nullable"),
  SPOTBUGS_NOT_NULL("edu.umd.cs.findbugs.annotations.NonNull"),
  // Spring
  SPRING_NULLABLE("org.springframework.lang.Nullable"),
  SPRING_NON_NULL("org.springframework.lang.NonNull");
  // TODO org.springframework.lang.NonNullApi

  private final NAAnnotation annotation;

  ProhibitedAnnotations(String fqcn) {
    this.annotation = NAAnnotation.of(fqcn);
  }

  public NAAnnotation getAnnotation() {
    return annotation;
  }

  public static final List<NAAnnotation> ALL = Arrays.stream(ProhibitedAnnotations.values())
      .map(ProhibitedAnnotations::getAnnotation)
      .toList();
}
