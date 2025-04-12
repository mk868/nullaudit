package eu.softpol.lib.nullaudit.coretest.rules.requirenullmarked;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RequireNullMarkedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("marked/SayHello.java").withContents("""
              package marked;
              
              import org.jspecify.annotations.NullMarked;
              
              @NullMarked
              public class SayHello {
              
                public void hello() {
                }
              }
              """)
          .createFile("unmarked/SayHello.java").withContents("""
              package unmarked;
              
              public class SayHello {
              
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
    var analyzer = new NullAuditAnalyzer(dir, RequireNullMarkedConfig.CONFIG);
    var report = analyzer.run();
    assertThat(report).issues().hasSize(1);
    assertThat(report).issuesForClass("unmarked", "SayHello").hasSize(1);
  }

}
