package eu.softpol.lib.nullaudit.coretest.signature;


import static com.google.common.truth.Truth.assertThat;

import eu.softpol.lib.nullaudit.core.signature.SignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.translator.RawStringTranslator;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldSignatureAnalyzerTest {

  public static Stream<Arguments> args() {
    return Stream.of(
        Arguments.of(
            "Ljava/util/stream/Collector<Ljava/lang/Object;*Ljava/util/Optional<Ljava/lang/Object;>;>;",
            "java.util.stream.Collector<java.lang.Object, ?, java.util.Optional<java.lang.Object>>"),
        Arguments.of("I", "int"),
        Arguments.of("Ljava/lang/String;", "java.lang.String"),
        Arguments.of("[I", "int[]"),
        Arguments.of("[Ljava/lang/String;", "java.lang.String[]"),
        Arguments.of("Ljava/util/List<Ljava/lang/String;>;", "java.util.List<java.lang.String>"),
        Arguments.of("Ljava/util/List<*>;", "java.util.List<?>"),
//        Arguments.of("Ljava/util/List<+Ljava/lang/Number;>;",
//            "java.util.List<? extends java.lang.Number>"),
        Arguments.of("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;",
            "java.util.Map<java.lang.String, java.lang.Integer>"),
//        Arguments.of("Ljava/util/Map<Ljava/lang/String;Ljava/util/List<+Ljava/lang/Number;>;>;",
//            "java.util.Map<String, java.util.List<? extends java.lang.Number>>"),
//        Arguments.of("Ljava/util/Map<-Ljava/lang/Integer;+Ljava/util/List<Ljava/lang/String;>;>;",
//            "java.util.Map<? super java.lang.Integer, ? extends java.util.List<String>>"),
//        Arguments.of("Ljava/util/List<+Ljava/util/List<-Ljava/lang/Integer;>;>;",
//            "java.util.List<? extends java.util.List<? super java.lang.Integer>>"),
//        Arguments.of(
//            "<T:Ljava/lang/Number;:Ljava/lang/Comparable<TT;>;>;",
//            "T extends java.lang.Number & java.lang.Comparable<T>"),
        Arguments.of(
            "LParent<Ljava/lang/String;>;",
            "Parent<java.lang.String>"),
        Arguments.of("I", "int"),

        Arguments.of("Ljava/util/List;", "java.util.List"),
//        Arguments.of("Ljava/util/List<-Ljava/lang/Integer;>;",
//            "java.util.List<? super java.lang.Integer>"),
        Arguments.of("Ljava/util/Map;", "java.util.Map"),

        Arguments.of("Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;",
            "java.util.List<java.util.List<java.lang.String>>")
//        Arguments.of("Ljava/util/Map<Ljava/lang/String;Ljava/util/List<+Ljava/lang/Number;>;>;",
//            "java.util.Map<java.lang.String, java.util.List<? extends java.lang.Number>>"),
//        Arguments.of("Ljava/util/Map<-Ljava/lang/Integer;+Ljava/util/List<Ljava/lang/String;>;>;",
//            "java.util.Map<? super java.lang.Integer, ? extends java.util.List<java.lang.String>>")
    );
  }

  @ParameterizedTest(name = "[{index}] {1}")
  @MethodSource("args")
  void test(String signature, String expected) {
    var typeNode = SignatureAnalyzer.analyzeFieldSignature(signature);
    assertThat(RawStringTranslator.INSTANCE.translate(typeNode)).isEqualTo(expected);
  }

}
