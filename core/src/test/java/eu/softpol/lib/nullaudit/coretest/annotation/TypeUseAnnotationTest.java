package eu.softpol.lib.nullaudit.coretest.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import org.junit.jupiter.api.Test;

class TypeUseAnnotationTest {

  @Test
  void test() {
    assertThat(NAAnnotation.fromDescriptor("Lorg/jspecify/annotations/Nullable;"))
        .isEqualTo(NAAnnotation.NULLABLE);
    assertThat(NAAnnotation.fromDescriptor("Lorg/jspecify/annotations/NonNull;"))
        .isEqualTo(NAAnnotation.NON_NULL);
  }
}
