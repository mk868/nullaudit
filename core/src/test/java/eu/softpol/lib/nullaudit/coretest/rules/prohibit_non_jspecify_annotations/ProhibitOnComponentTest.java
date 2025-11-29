package eu.softpol.lib.nullaudit.coretest.rules.prohibit_non_jspecify_annotations;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.ProhibitNonJSpecifyAnnotations;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ProhibitOnComponentTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .also()
          .createFile("test/MyJavaxRecord.java").withContents("""
              package test;
              
              import javax.annotation.Nullable;
              
              public record MyJavaxRecord(
                  @Nullable String s1
              ) {}
              """)
          .createFile("test/MyJetBrainsRecord.java").withContents("""
              package test;
              
              import java.util.List;
              import org.jetbrains.annotations.Nullable;
              
              public record MyJetBrainsRecord(
                  List<@Nullable String> s1
              ) {}
              """)
          .createFile("test/MultiAnnotated.java").withContents("""
              package test;
              
              import java.util.List;
              
              public record MultiAnnotated(
                  @javax.annotation.Nullable
                  @org.jetbrains.annotations.Nullable
                  List<@org.checkerframework.checker.nullness.qual.Nullable String> s
              ) {}
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldDetectAnnotationOnComponent() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJavaxRecord")
        .hasSize(1)
        .first()
        .messageContains("javax.annotation.Nullable");
  }

  @Test
  void shouldDetectAnnotationOnComponentType() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJetBrainsRecord")
        .hasSize(1)
        .first()
        .messageContains("org.jetbrains.annotations.Nullable");
  }

  @Test
  void shouldDetectMultipleAnnotations() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MultiAnnotated")
        .hasSize(1)
        .first()
        .messageContains("org.jetbrains.annotations.Nullable")
        .messageContains("javax.annotation.Nullable")
        .messageContains("org.checkerframework.checker.nullness.qual.Nullable");
  }

}
