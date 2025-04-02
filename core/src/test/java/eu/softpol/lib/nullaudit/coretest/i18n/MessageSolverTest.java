package eu.softpol.lib.nullaudit.coretest.i18n;

import static org.assertj.core.api.Assertions.assertThat;

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
    var msg = messageSolver.issueUnspecifiedNullnessClass(signature, positions);
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
    var msg = messageSolver.issueUnspecifiedNullnessField(signature, positions);
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
    var msg = messageSolver.issueUnspecifiedNullnessComponent(signature, positions);
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
    var msg = messageSolver.issueUnspecifiedNullnessMethod(signature, positions);
    // THEN
    assertThat(msg).contains(signature);
    assertThat(msg).contains(positions);

    assertThat(indexesOf(lineWithContent(msg, signature), '*'))
        .isEqualTo(indexesOf(lineWithContent(msg, positions), '^'));
  }

  @Test
  void issueIrrelevantAnnotationNullUnMarkedPackageTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationNullUnMarkedPackage();
    // THEN
    assertThat(msg).contains("package");
  }

  @Test
  void issueIrrelevantAnnotationNullUnMarkedClassTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationNullUnMarkedClass();
    // THEN
    assertThat(msg).contains("class");
  }

  @Test
  void issueIrrelevantAnnotationNullUnMarkedMethodTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationNullUnMarkedMethod();
    // THEN
    assertThat(msg).contains("method");
  }

  @Test
  void issueIrrelevantAnnotationOnPrimitiveComponentTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationOnPrimitiveComponent();
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
  }

  @Test
  void issueIrrelevantAnnotationOnPrimitiveFieldTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationOnPrimitiveField();
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
  }

  @Test
  void issueIrrelevantAnnotationOnPrimitiveMethodTest() {
    // GIVEN/WHEN
    var msg = messageSolver.issueIrrelevantAnnotationOnPrimitiveMethod();
    // THEN
    assertThat(msg).containsIgnoringCase("primitive");
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
