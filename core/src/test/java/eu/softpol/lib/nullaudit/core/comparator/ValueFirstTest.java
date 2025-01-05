package eu.softpol.lib.nullaudit.core.comparator;

import static eu.softpol.lib.nullaudit.core.comparator.ValueFirst.valueFirst;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ValueFirstTest {

  @Test
  void test() {
    var input = List.of(
        "aaa",
        "bbb",
        "bob",
        "ccc",
        "ddd"
    );
    var comparator = valueFirst("bob", String.CASE_INSENSITIVE_ORDER);

    var sorted = input.stream().sorted(comparator).toList();

    assertThat(sorted).containsExactly(
        "bob",
        "aaa",
        "bbb",
        "ccc",
        "ddd"
    );
  }

  @Test
  void test2() {
    var input = List.of(
        "aaa",
        "bbb",
        "bob",
        "ccc",
        "ddd",
        "bob"
    );
    var comparator = valueFirst("bob", String.CASE_INSENSITIVE_ORDER);

    var sorted = input.stream().sorted(comparator).toList();

    assertThat(sorted).containsExactly(
        "bob",
        "bob",
        "aaa",
        "bbb",
        "ccc",
        "ddd"
    );
  }

}
