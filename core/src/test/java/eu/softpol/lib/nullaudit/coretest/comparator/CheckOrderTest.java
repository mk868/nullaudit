package eu.softpol.lib.nullaudit.coretest.comparator;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.comparator.CheckOrder;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

class CheckOrderTest {

  static final List<String> EXTENSIONS = List.of(
      ".class",
      "" // when raw simpleClassName provided
  );

  @ParameterizedTest
  @FieldSource("EXTENSIONS")
  void shouldCheckModuleInfoFirst(String ext) {
    var input = List.of(
        "aaa" + ext,
        "module-info" + ext,
        "bbb" + ext,
        "package-info" + ext,
        "ccc" + ext
    );
    var sorted = input.stream().sorted(CheckOrder.COMPARATOR).toList();

    assertThat(sorted).containsExactly(
        "module-info" + ext,
        "package-info" + ext,
        "aaa" + ext,
        "bbb" + ext,
        "ccc" + ext
    );
  }

  @ParameterizedTest
  @FieldSource("EXTENSIONS")
  void shouldCheckPackageInfoFirst(String ext) {
    var input = List.of(
        "aaa" + ext,
        "bbb" + ext,
        "package-info" + ext,
        "ccc" + ext
    );
    var sorted = input.stream().sorted(CheckOrder.COMPARATOR).toList();

    assertThat(sorted).containsExactly(
        "package-info" + ext,
        "aaa" + ext,
        "bbb" + ext,
        "ccc" + ext
    );
  }

  @ParameterizedTest
  @FieldSource("EXTENSIONS")
  void shouldCheckOuterClassBeforeInnerClass(String ext) {
    var input = List.of(
        "bbb" + ext,
        "aaa$inner1" + ext,
        "aaa$inner1$innerA" + ext,
        "aaa$inner2" + ext,
        "aaa" + ext,
        "ccc" + ext
    );
    var sorted = input.stream().sorted(CheckOrder.COMPARATOR).toList();

    assertThat(sorted).containsExactly(
        "aaa" + ext,
        "aaa$inner1" + ext,
        "aaa$inner1$innerA" + ext,
        "aaa$inner2" + ext,
        "bbb" + ext,
        "ccc" + ext
    );
  }
}
