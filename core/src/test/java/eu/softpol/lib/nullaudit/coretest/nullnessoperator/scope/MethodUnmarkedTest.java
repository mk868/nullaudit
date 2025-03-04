package eu.softpol.lib.nullaudit.coretest.nullnessoperator.scope;

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

class MethodUnmarkedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("root/scope/modulemarked/Prefix1.java").withContents("""
              package root.scope.modulemarked;
              
              public class Prefix1 {
              
                public String addPrefix(String str) {
                  return "prefix:" + str;
                }
              }
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldBeInNullMarkedScopeWhenModuleInfoAnnotatedWithNullMarked() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();
    assertThat(report.issues()).isNotEmpty();
  }

}
