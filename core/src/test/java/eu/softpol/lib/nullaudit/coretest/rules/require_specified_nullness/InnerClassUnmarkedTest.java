package eu.softpol.lib.nullaudit.coretest.rules.require_specified_nullness;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.coretest.rules.RulesConfig;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class InnerClassUnmarkedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("root/scope/innerclassunmarked/Prefix1.java").withContents("""
              package root.scope.innerclassunmarked;
              
              import org.jspecify.annotations.NullMarked;
              import org.jspecify.annotations.NullUnmarked;
              
              @NullMarked
              public class Prefix1 {
              
                @NullUnmarked
                public class Inner {
              
                  public String addPrefix(String str) {
                    return "prefix:" + str;
                  }
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
    var analyzer = new NullAuditAnalyzer(dir, RulesConfig.REQUIRE_SPECIFIED_NULLNESS);
    var report = analyzer.run();
    assertThat(report).issues().isNotEmpty();
  }

}
