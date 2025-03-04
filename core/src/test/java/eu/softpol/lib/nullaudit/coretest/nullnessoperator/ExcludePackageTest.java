package eu.softpol.lib.nullaudit.coretest.nullnessoperator;

import static com.google.common.truth.Truth.assertThat;
import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExcludePackageTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("root/exclude/Exclude1.java").withContents("""
              package root.exclude;
              
              public class Exclude1 {
              
                public String addPrefix(String str) {
                  return "prefix:" + str;
                }
              }
              """
          );
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
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
