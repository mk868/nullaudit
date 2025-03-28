package eu.softpol.lib.nullaudit.coretest;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReportTest {

  public static Stream<Arguments> source() {
    return Stream.of(
        Arguments.of(Resources.SAMPLE1_JAR, Resources.SAMPLE1_CLASSES)
    );
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("source")
  void shouldGenerateSameReportForJarAndDirSource(Path jarPath, Path dirPath) {
    var jarAnalyzer = new NullAuditAnalyzer(jarPath, List.of());
    var jarReport = jarAnalyzer.run();

    var dirAnalyzer = new NullAuditAnalyzer(dirPath, List.of());
    var dirReport = dirAnalyzer.run();

    assertThat(jarReport).isEqualTo(dirReport);
  }
}
