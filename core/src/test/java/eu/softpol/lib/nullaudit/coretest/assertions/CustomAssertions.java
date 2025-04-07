package eu.softpol.lib.nullaudit.coretest.assertions;

import eu.softpol.lib.nullaudit.core.report.Report;
import org.assertj.core.api.Assertions;

public class CustomAssertions extends Assertions {

  public static ReportAssert assertThat(Report actual) {
    return new ReportAssert(actual);
  }
}
