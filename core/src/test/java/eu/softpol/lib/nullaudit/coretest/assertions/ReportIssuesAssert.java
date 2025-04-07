package eu.softpol.lib.nullaudit.coretest.assertions;

import eu.softpol.lib.nullaudit.core.report.Issue;
import java.util.List;
import java.util.stream.StreamSupport;
import org.assertj.core.api.AbstractListAssert;

public class ReportIssuesAssert
    extends AbstractListAssert<ReportIssuesAssert, List<Issue>, Issue, IssueAssert> {

  protected ReportIssuesAssert(List<Issue> issues) {
    super(issues, ReportIssuesAssert.class);
  }

  @Override
  protected IssueAssert toAssert(Issue value, String description) {
    return new IssueAssert(value);
  }

  @Override
  protected ReportIssuesAssert newAbstractIterableAssert(Iterable<? extends Issue> iterable) {
    var values = StreamSupport.stream(iterable.spliterator(), false)
        .map(Issue.class::cast)
        .toList();
    return new ReportIssuesAssert(values);
  }
}
