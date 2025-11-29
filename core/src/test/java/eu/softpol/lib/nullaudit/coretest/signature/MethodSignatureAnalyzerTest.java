package eu.softpol.lib.nullaudit.coretest.signature;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.signature.MethodSignatureAnalyzer;
import eu.softpol.lib.nullaudit.core.type.translator.RawStringTranslator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MethodSignatureAnalyzerTest {

  public static Stream<Arguments> args() {
    return Stream.of(
        Arguments.of("()V", "void method()"),
        Arguments.of("(I)V", "void method(int)"),
        Arguments.of("(D)V", "void method(double)"),
        Arguments.of("(Ljava/lang/String;)V", "void method(java.lang.String)"),
        Arguments.of("(I)Ljava/lang/String;", "java.lang.String method(int)"),
        Arguments.of("(I)I", "int method(int)"),
        Arguments.of("(II)V", "void method(int, int)"),
        Arguments.of("(ID)V", "void method(int, double)"),

        Arguments.of("([I)V", "void method(int[])"),
        Arguments.of("([[I)V", "void method(int[][])"),
        Arguments.of("([Ljava/lang/String;)V", "void method(java.lang.String[])"),
        Arguments.of("([[Ljava/lang/String;)V", "void method(java.lang.String[][])"),

        Arguments.of("(Ljava/util/List;)V", "void method(java.util.List)"),
        Arguments.of("(Ljava/util/Map;)V", "void method(java.util.Map)"),
        Arguments.of("(Ljava/util/Map;Ljava/util/List;)V",
            "void method(java.util.Map, java.util.List)"),

        Arguments.of("(Ljava/util/List<Ljava/lang/String;>;)V",
            "void method(java.util.List<java.lang.String>)"),
        Arguments.of("(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;)V",
            "void method(java.util.Map<java.lang.String, java.lang.Integer>)"),
        Arguments.of("(Ljava/util/List<+Ljava/lang/Number;>;)V",
            "void method(java.util.List<? extends java.lang.Number>)"),
        Arguments.of("(Ljava/util/List<-Ljava/lang/Integer;>;)V",
            "void method(java.util.List<? super java.lang.Integer>)"),

        Arguments.of("()TT;", "T method()"),
        Arguments.of("(TT;)TT;", "T method(T)"),

        Arguments.of("(Ljava/util/List<Ljava/util/List<Ljava/lang/String;>;>;)V",
            "void method(java.util.List<java.util.List<java.lang.String>>)"),
        Arguments.of("(Ljava/util/Map<Ljava/lang/String;Ljava/util/List<+Ljava/lang/Number;>;>;)V",
            "void method(java.util.Map<java.lang.String, java.util.List<? extends java.lang.Number>>)"),
        Arguments.of("(Ljava/util/Map<Ljava/lang/String;Ljava/util/List<-Ljava/lang/Integer;>;>;)V",
            "void method(java.util.Map<java.lang.String, java.util.List<? super java.lang.Integer>>)"),

        Arguments.of("(Ljava/util/List<*>;)V", "void method(java.util.List<?>)"),
        Arguments.of("(Ljava/util/List<+Ljava/lang/Number;>;)Ljava/util/List<Ljava/lang/Number;>;",
            "java.util.List<java.lang.Number> method(java.util.List<? extends java.lang.Number>)"),
        Arguments.of(
            "(Ljava/util/List<-Ljava/lang/Integer;>;)Ljava/util/List<Ljava/lang/Integer;>;",
            "java.util.List<java.lang.Integer> method(java.util.List<? super java.lang.Integer>)"),

        Arguments.of("(Ljava/lang/String;I)Ljava/lang/String;",
            "java.lang.String method(java.lang.String, int)"),
        Arguments.of("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
            "java.lang.String method(java.lang.String, java.lang.String)"),
        Arguments.of("(Ljava/lang/String;I[D)V", "void method(java.lang.String, int, double[])"),
        Arguments.of("(Ljava/util/List;Ljava/util/Map;)V",
            "void method(java.util.List, java.util.Map)"),

        Arguments.of("()[I", "int[] method()"),
        Arguments.of("()[[Ljava/lang/String;", "java.lang.String[][] method()"),
        Arguments.of("(I)[Ljava/lang/String;", "java.lang.String[] method(int)"),
        Arguments.of("(Ljava/lang/String;)[[I", "int[][] method(java.lang.String)"),

        Arguments.of("()Ljava/util/List<Ljava/lang/String;>;",
            "java.util.List<java.lang.String> method()"),
        Arguments.of("()Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;",
            "java.util.Map<java.lang.String, java.lang.Integer> method()"),
        Arguments.of("()Ljava/util/List<+Ljava/lang/Number;>;",
            "java.util.List<? extends java.lang.Number> method()"),
        Arguments.of("()Ljava/util/Map<Ljava/lang/String;Ljava/util/List<+Ljava/lang/Number;>;>;",
            "java.util.Map<java.lang.String, java.util.List<? extends java.lang.Number>> method()"),
// TODO
//        Arguments.of("<T:Ljava/lang/Number;>(TT;)Ljava/util/List<TT;>;",
//            "<T extends java.lang.Number> java.util.List<T> method(T)"),
//        Arguments.of("<T:>(TT;)Ljava/util/List<TT;>;", "<T> java.util.List<T> method(T)")

        Arguments.of("()[Ljava/util/List<Ljava/lang/String;>;",
            "java.util.List<java.lang.String>[] method()"),
        Arguments.of("()V^Ljava/io/IOException;", "void method()")
        );
  }

  @ParameterizedTest(name = "[{index}] {1}")
  @MethodSource("args")
  void test(String signature, String expected) {
    var s = MethodSignatureAnalyzer.analyze(signature);
    var str = RawStringTranslator.INSTANCE.translate(s.returnType()) + " method" +
              s.parameterTypes().stream()
                  .map(RawStringTranslator.INSTANCE::translate)
                  .collect(Collectors.joining(", ", "(", ")"));
    assertThat(str).isEqualTo(expected);
  }

}
