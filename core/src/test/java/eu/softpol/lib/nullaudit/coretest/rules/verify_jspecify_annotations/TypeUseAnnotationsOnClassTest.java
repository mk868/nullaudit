package eu.softpol.lib.nullaudit.coretest.rules.verify_jspecify_annotations;

import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;
import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.coretest.rules.RulesConfig;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TypeUseAnnotationsOnClassTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("irrelevant/Prefix1.java").withContents("""
              package irrelevant;
              
              import org.jspecify.annotations.Nullable;
              
              @Nullable
              public class Prefix1 {
              }
              """)
          .createFile("irrelevant/Prefix2.java").withContents("""
              package irrelevant;
              
              import org.jspecify.annotations.NonNull;
              
              @NonNull
              public record Prefix2() {
              }
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldReportNullableAnnotationsOnPrimitives() {
    var analyzer = new NullAuditAnalyzer(dir, RulesConfig.VERIFY_JSPECIFY_ANNOTATIONS);
    var report = analyzer.run();
    assertThat(report.issues())
        .hasSize(2);
  }

}
