package eu.softpol.lib.nullaudit.coretest;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RecordTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("com/example/SimpleRecord.java").withContents("""
              package com.example;
              
              import java.util.List;
              import org.jspecify.annotations.Nullable;
              
              record SimpleRecord(
                  List<String> a,
                  String b,
                  @Nullable List<@Nullable String> c
              ){
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
    assertThat(summary.totalClasses()).isEqualTo(1);
    assertThat(summary.totalFields()).isEqualTo(3);
    assertThat(summary.unspecifiedNullness().classes()).isEqualTo(1);
    assertThat(summary.unspecifiedNullness().fields()).isEqualTo(2);
    assertThat(report).totalNumberOfIssues(2);
    assertThat(report).issuesForField("com.example", "SimpleRecord", "a")
        .singleElement()
        .messageContains("java.util.List*<java.lang.String*> a");
    assertThat(report).issuesForField("com.example", "SimpleRecord", "b")
        .singleElement()
        .messageContains("String* b");
  }

}
