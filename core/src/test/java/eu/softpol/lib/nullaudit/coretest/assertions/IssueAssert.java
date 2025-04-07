package eu.softpol.lib.nullaudit.coretest.assertions;

import eu.softpol.lib.nullaudit.core.report.Issue;
import eu.softpol.lib.nullaudit.core.report.Kind;
import org.assertj.core.api.AbstractAssert;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

  protected IssueAssert(Issue issue) {
    super(issue, IssueAssert.class);
  }

  public IssueAssert hasMessage(String expected) {
    isNotNull();
    if (!actual.message().equals(expected)) {
      failWithMessage("Expected message to be <%s> but was <%s>", expected, actual.message());
    }
    return this;
  }

  public IssueAssert messageContains(String expected) {
    isNotNull();
    if (!actual.message().contains(expected)) {
      failWithMessage("Expected message to contain <%s> but was <%s>", expected, actual.message());
    }
    return this;
  }

  public IssueAssert hasKind(Kind expected) {
    isNotNull();
    if (!actual.kind().equals(expected)) {
      failWithMessage("Expected kind to be <%s> but was <%s>", expected, actual.kind());
    }
    return this;
  }
}
