package eu.softpol.lib.nullaudit.coretest;

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

class MethodTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("com/example/Complex.java").withContents("""
              package com.example;
              
              import java.util.List;
              import java.util.Map;
              import org.jspecify.annotations.Nullable;
              import org.jspecify.annotations.NonNull;
              
              class Complex {
              
                  public @Nullable String ok4(
                        @Nullable List<@NonNull String> list,
                        @Nullable Map<@NonNull String, @NonNull List<@Nullable Integer>> map,
                        @Nullable String @NonNull [] strAr8ray,
                        @Nullable List<@Nullable String> @Nullable [] strArray,
                        @Nullable List<@Nullable String @Nullable []> strAr,
                        @Nullable String @Nullable [] @NonNull [] strArrayArray,
                        int @Nullable [] ssss,
                        @Nullable Integer @Nullable ... iii
                  ) {
                      return "";
                  }
              }
              """)
          .createFile("com/example/Simple.java").withContents("""
              package com.example;
              
              import org.jspecify.annotations.NonNull;
              import org.jspecify.annotations.Nullable;
              
              class Simple {
              
                  public boolean isNotBlank(@Nullable String str) {
                      return str != null && str.trim().length() > 0;
                  }
              
                  public boolean isBlank(@NonNull String str) {
                      return str.trim().length() == 0;
                  }
              
                  public @NonNull String toUpperCase(@Nullable String str) {
                      return str == null ? "" : str.toUpperCase();
                  }
              }
              """)
          .createFile("com/example/Invalid.java").withContents("""
              package com.example;
              
              class Invalid {
              
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
  void test() {
    var analyzer = new NullAuditAnalyzer(dir, List.of());
    var report = analyzer.run();

    var summary = report.summary();
    assertThat(summary.totalClasses()).isEqualTo(3);
    //TODO assertThat(summary.totalMethods()).isEqualTo(5);
    assertThat(summary.unspecifiedNullness().classes()).isEqualTo(1);
    assertThat(summary.unspecifiedNullness().methods()).isEqualTo(1);
    var issues = report.issues();
    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).location()).contains("com.example.Invalid#addPrefix(java.lang.String)");
    assertThat(issues.get(0).message()).contains("java.lang.String* addPrefix(java.lang.String*)");
  }

}
