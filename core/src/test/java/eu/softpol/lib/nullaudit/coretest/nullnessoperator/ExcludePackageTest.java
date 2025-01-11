package eu.softpol.lib.nullaudit.coretest.nullnessoperator;

import static com.google.common.truth.Truth.assertThat;
import static eu.softpol.lib.nullaudit.coretest.Resources.SAMPLE1_CLASSES;
import static eu.softpol.lib.nullaudit.coretest.nullnessoperator.SetupProject.setup;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExcludePackageTest {

  @TempDir
  Path dir;

  @BeforeEach
  void init() {
    setup(
        SAMPLE1_CLASSES,
        List.of("root/exclude"),
        dir
    );
  }

  @Test
  void shouldExcludePackage() {
    var analyzer = new NullAuditAnalyzer(dir, List.of("root.exclude"));
    var report = analyzer.run();
    assertThat(report.issues()).isEmpty();
  }

  @Test
  void shouldNotExcludePackage() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();
    assertThat(report.issues()).isNotEmpty();
  }
}
