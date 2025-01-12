package eu.softpol.lib.nullaudit.coretest.signature;


import static com.google.common.truth.Truth.assertThat;

import eu.softpol.lib.nullaudit.core.signature.SignatureAnalyzer;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldSignatureAnalyzerTest {

  public static Stream<Arguments> args() {
    return Stream.of(
        Arguments.of(
            "Ljava/util/stream/Collector<Ljava/lang/Object;*Ljava/util/Optional<Ljava/lang/Object;>;>;",
            "java.util.stream.Collector*<java.lang.Object*, ?, java.util.Optional*<java.lang.Object*>>"
        )
    );
  }

  @ParameterizedTest
  @MethodSource("args")
  void test(String signature, String expected) {
    var typeNode = SignatureAnalyzer.analyzeFieldSignature(signature);
    assertThat("" + typeNode).isEqualTo(expected);
  }

}
