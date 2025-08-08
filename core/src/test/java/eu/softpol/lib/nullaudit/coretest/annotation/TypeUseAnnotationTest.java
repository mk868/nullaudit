package eu.softpol.lib.nullaudit.coretest.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import org.junit.jupiter.api.Test;

class TypeUseAnnotationTest {

  @Test
  void test() {
    assertThat(TypeUseAnnotation.ofDescriptor("Lorg/jspecify/annotations/Nullable;"))
        .isEqualTo(TypeUseAnnotation.JSPECIFY_NULLABLE);
    assertThat(TypeUseAnnotation.ofDescriptor("Lorg/jspecify/annotations/NonNull;"))
        .isEqualTo(TypeUseAnnotation.JSPECIFY_NON_NULL);
  }
}
