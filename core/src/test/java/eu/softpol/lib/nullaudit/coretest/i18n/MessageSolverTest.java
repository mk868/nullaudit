package eu.softpol.lib.nullaudit.coretest.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

  @Test
  void invalidNullMarkCombinationPackageTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_PACKAGE);
    // THEN
    assertThat(msg).contains("package");
  }

  @Test
  void invalidNullMarkCombinationClassTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_CLASS);
    // THEN
    assertThat(msg).contains("class");
  }

  @Test
  void invalidNullMarkCombinationMethodTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULL_MARK_COMBINATION_METHOD);
    // THEN
    assertThat(msg).contains("method");
  }

  @Test
  void invalidNullnessOnPrimitiveComponentTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_COMPONENT);
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
  }

  @Test
  void invalidNullnessOnPrimitiveFieldTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_FIELD);
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
  }

  @Test
  void invalidNullnessOnPrimitiveMethodTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_INVALID_NULLNESS_ON_PRIMITIVE_METHOD);
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
  }

  @Test
  void missingNullMarkedAnnotationClassTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.ISSUE_MISSING_NULLMARKED_ANNOTATION_CLASS);
    // THEN
    assertThat(msg).containsIgnoringCase("@NullMarked");
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
