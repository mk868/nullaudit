package eu.softpol.lib.nullaudit.core.nullnessoperator.scope;

import static eu.softpol.lib.nullaudit.core.Resources.SAMPLE1_CLASSES;
import static eu.softpol.lib.nullaudit.core.nullnessoperator.SetupProject.setup;
import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class InnerClassMarkedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void init() {
    setup(
        SAMPLE1_CLASSES,
        List.of(
            "root/scope/innerclassmarked"
        ),
        dir
    );
  }

  @Test
  void shouldBeInNullMarkedScopeWhenModuleInfoAnnotatedWithNullMarked() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();
    assertThat(report.problems()).isEmpty();
  }

}