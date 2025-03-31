package eu.softpol.lib.nullaudit.coretest.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Disabled
class TypeUseAnnotationTest {

  @ParameterizedTest
  @CsvSource(value = {
      "Lorg/jspecify/annotations/Nullable;, org.jspecify.annotations.Nullable",
      "Lorg/jspecify/annotations/NonNull;,  org.jspecify.annotations.NonNull",
  })
  void test(String descriptor, String clazz) {
    assertThat(TypeUseAnnotation.ofDescriptor(descriptor))
        .isEqualTo(TypeUseAnnotation.ofClazz(clazz));
  }
}
