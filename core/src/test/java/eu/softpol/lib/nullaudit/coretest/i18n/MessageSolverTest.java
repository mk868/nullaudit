package eu.softpol.lib.nullaudit.coretest.i18n;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MessageSolverTest {

  MessageSolver messageSolver;

  @BeforeEach
  void init() {
    messageSolver = new MessageSolver();
  }

  @Test
  void issueUnspecifiedNullnessClassTest() {
    // GIVEN
    String signature = "<T extends Object*, T2 extends Number*> className";
    String positions = "                 ^                   ^           ";
    // WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_CLASS, signature,
        positions);
    // THEN
    assertThat(msg).contains(signature);
    assertThat(msg).contains(positions);

    assertThat(indexesOf(lineWithContent(msg, signature), '*'))
        .isEqualTo(indexesOf(lineWithContent(msg, positions), '^'));
  }


  @Test
  void issueUnspecifiedNullnessFieldTest() {
    // GIVEN
    String signature = "List*<String*> fieldName";
    String positions = "    ^       ^           ";
    // WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_FIELD, signature,
        positions);
    // THEN
    assertThat(msg).contains(signature);
    assertThat(msg).contains(positions);

    assertThat(indexesOf(lineWithContent(msg, signature), '*'))
        .isEqualTo(indexesOf(lineWithContent(msg, positions), '^'));
  }

  @Test
  void issueUnspecifiedNullnessComponentTest() {
    // GIVEN
    String signature = "List*<String*> componentName";
    String positions = "    ^       ^               ";
    // WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_COMPONENT, signature,
        positions);
    // THEN
    assertThat(msg).contains(signature);
    assertThat(msg).contains(positions);

    assertThat(indexesOf(lineWithContent(msg, signature), '*'))
        .isEqualTo(indexesOf(lineWithContent(msg, positions), '^'));
  }

  @Test
  void issueUnspecifiedNullnessMethodTest() {
    // GIVEN
    String signature = "List*<String*> methodName()";
    String positions = "    ^       ^              ";
    // WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_METHOD, signature,
        positions);
    // THEN
    assertThat(msg).contains(signature);
    assertThat(msg).contains(positions);

    assertThat(indexesOf(lineWithContent(msg, signature), '*'))
        .isEqualTo(indexesOf(lineWithContent(msg, positions), '^'));
  }

  @ParameterizedTest
  @EnumSource(MessageKey.class)
  void noArgTest(MessageKey messageKey) {
    assumeThat(messageKey.argCount())
        .isEqualTo(0);

    // GIVEN/WHEN
    var msg = messageSolver.resolve(messageKey);
    // THEN
    assertThat(msg).isNotBlank();
  }

  static String lineWithContent(String str, String content) {
    return str.lines()
        .filter(a -> a.contains(content))
        .findFirst()
        .orElseThrow();
  }

  static Set<Integer> indexesOf(String str, int ch) {
    Set<Integer> positions = new HashSet<>();
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == ch) {
        positions.add(i);
      }
    }
    return Set.copyOf(positions);
  }
}
