package eu.softpol.lib.nullaudit.maventest.i18n;


import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.maven.i18n.MessageKey;
import eu.softpol.lib.nullaudit.maven.i18n.MessageSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageSolverTest {

  MessageSolver messageSolver;

  @BeforeEach
  void init() {
    messageSolver = new MessageSolver();
  }

  @Test
  void checkNoIssuesFoundTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, 0);
    // THEN
    assertThat(msg).isEqualTo("No issues found.");
  }

  @Test
  void checkOneIssueFoundTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, 1);
    // THEN
    assertThat(msg).isEqualTo("1 issue found.");
  }

  @Test
  void checkMultipleIssuesFoundTest() {
    // GIVEN
    int number = 30;
    // WHEN
    var msg = messageSolver.resolve(MessageKey.CHECK_ISSUES_FOUND, number);
    // THEN
    assertThat(msg).isEqualTo(number + " issues found.");
  }

  @Test
  void checkOneMoreIssueTest() {
    // GIVEN/WHEN
    var msg = messageSolver.resolve(MessageKey.CHECK_MORE_ISSUES, 1);
    // THEN
    assertThat(msg).contains("1 more issue.");
  }

  @Test
  void checkMultipleMoreIssuesTest() {
    // GIVEN
    int number = 30;
    // WHEN
    var msg = messageSolver.resolve(MessageKey.CHECK_MORE_ISSUES, number);
    // THEN
    assertThat(msg).contains(number + " more issues.");
  }

  @Test
  void reportReportSavedTest() {
    // GIVEN
    var path = "/home/user/report.json";
    // WHEN
    var msg = messageSolver.resolve(MessageKey.REPORT_REPORT_SAVED, path);
    // THEN
    assertThat(msg).contains(path);
  }

}
