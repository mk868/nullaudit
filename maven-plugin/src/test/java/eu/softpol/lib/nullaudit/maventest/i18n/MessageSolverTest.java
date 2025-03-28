package eu.softpol.lib.nullaudit.maventest.i18n;


import static org.assertj.core.api.Assertions.assertThat;

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
    var msg = messageSolver.checkNoIssuesFound();
    // THEN
    assertThat(msg).isNotEmpty();
  }

  @Test
  void checkOneIssueFoundTest() {
    // GIVEN/WHEN
    var msg = messageSolver.checkOneIssueFound();
    // THEN
    assertThat(msg).contains("1");
  }

  @Test
  void checkMultipleIssuesFoundTest() {
    // GIVEN
    int number = 30;
    // WHEN
    var msg = messageSolver.checkMultipleIssuesFound(number);
    // THEN
    assertThat(msg).contains(number + "");
  }

  @Test
  void checkOneMoreIssueTest() {
    // GIVEN/WHEN
    var msg = messageSolver.checkOneMoreIssue();
    // THEN
    assertThat(msg).contains("1");
  }

  @Test
  void checkMultipleMoreIssuesTest() {
    // GIVEN
    int number = 30;
    // WHEN
    var msg = messageSolver.checkMultipleMoreIssues(number);
    // THEN
    assertThat(msg).contains(number + "");
  }

  @Test
  void reportReportSavedTest() {
    // GIVEN
    var path = "/home/user/report.json";
    // WHEN
    var msg = messageSolver.reportReportSaved(path);
    // THEN
    assertThat(msg).contains(path);
  }

}
