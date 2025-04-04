package eu.softpol.lib.nullaudit.coretest.irrelevant.marked;

import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;
import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class IrrelevantMarkedMethodTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("irrelevant/marked/SayHello.java").withContents("""
              package irrelevant.marked;
              
              import org.jspecify.annotations.NullMarked;
              import org.jspecify.annotations.NullUnmarked;
              
              public class SayHello {
              
                @NullMarked
                @NullUnmarked
                public void hello() {
                }
              }
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void test() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();
    assertThat(report.issues()).hasSize(1);
  }

}
