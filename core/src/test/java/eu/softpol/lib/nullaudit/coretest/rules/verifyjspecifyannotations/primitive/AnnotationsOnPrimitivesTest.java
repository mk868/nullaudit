package eu.softpol.lib.nullaudit.coretest.rules.verifyjspecifyannotations.primitive;

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

class AnnotationsOnPrimitivesTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("irrelevant/marked/primitive/Prefix1.java").withContents("""
              package irrelevant.marked.primitive;
              
              import org.jspecify.annotations.NonNull;
              import org.jspecify.annotations.NullMarked;
              import org.jspecify.annotations.Nullable;
              
              @NullMarked
              public class Prefix1 {
                @Nullable int[] i;
                @NonNull short[] s;
                @Nullable double[] d;
                @NonNull float[] f;
                @NonNull byte[] b;
                @Nullable boolean[] z;
              
                void method(
                  @Nullable int i_array,
                  @Nullable short s_array,
                  @Nullable double d_array,
                  @Nullable float f_array,
                  @Nullable byte b_array,
                  @Nullable boolean z_array
                ) {
                }
              }
              """)
          .createFile("irrelevant/marked/primitive/Prefix2.java").withContents("""
              package irrelevant.marked.primitive;
              
              import org.jspecify.annotations.NullMarked;
              import org.jspecify.annotations.Nullable;
              
              @NullMarked
              public record Prefix2(
                @Nullable int i,
                @Nullable short s,
                @Nullable double d,
                @Nullable float f,
                @Nullable byte b,
                @Nullable boolean z
              ){
              }
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldReportNullableAnnotationsOnPrimitives() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();
    assertThat(report.issues())
        .hasSize(13);
  }

}
