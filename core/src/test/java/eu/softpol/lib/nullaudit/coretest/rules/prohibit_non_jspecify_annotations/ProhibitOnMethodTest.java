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

public class ProhibitOnMethodTest {

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
          .createFile("test/MyJavaxClass1.java").withContents("""
              package test;
              
              import javax.annotation.Nullable;
              
              public class MyJavaxClass1 {
                  @Nullable
                  String s1() {
                    return "";
                  }
              }
              """)
          .createFile("test/MyJavaxClass2.java").withContents("""
              package test;
              
              import javax.annotation.Nullable;
              
              public class MyJavaxClass2 {
                  String s1(@Nullable String arg1) {
                    return "";
                  }
              }
              """)
          .createFile("test/MyJetBrainsClass1.java").withContents("""
              package test;
              
              import java.util.List;
              import org.jetbrains.annotations.Nullable;
              
              public class MyJetBrainsClass1 {
                  List<@Nullable String> s1(){
                    return List.of();
                  }
              }
              """)
          .createFile("test/MyJetBrainsClass2.java").withContents("""
              package test;
              
              import java.util.List;
              import org.jetbrains.annotations.Nullable;
              
              public class MyJetBrainsClass2 {
                  String s1(List<@Nullable String> arg1){
                    return "";
                  }
              }
              """)
          .createFile("test/MyJavaxClass3.java").withContents("""
              package test;
              
              import javax.annotation.Nullable;
              
              public class MyJavaxClass3 {
                  @Nullable
                  String s1() {
                    return "";
                  }
              }
              """)
          .createFile("test/MultiAnnotated.java").withContents("""
              package test;
              
              import java.util.List;
              
              public class MultiAnnotated {
                  @javax.annotation.Nullable
                  @org.jetbrains.annotations.Nullable
                  List<@org.checkerframework.checker.nullness.qual.Nullable String> s1() {
                    return List.of();
                  };
              }
              """);
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldDetectAnnotationOnMethod() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJavaxClass3")
        .hasSize(1)
        .first()
        .messageContains("javax.annotation.Nullable");
  }

  @Test
  void shouldDetectAnnotationOnMethodReturnType() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJetBrainsClass1")
        .hasSize(1)
        .first()
        .messageContains("org.jetbrains.annotations.Nullable");
  }

  @Test
  void shouldDetectAnnotationOnMethodParam() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJavaxClass2")
        .hasSize(1)
        .first()
        .messageContains("javax.annotation.Nullable");
  }

  @Test
  void shouldDetectAnnotationOnMethodParamType() {
    var config = NullAuditConfig.of()
        .withProhibitNonJSpecifyAnnotations(new ProhibitNonJSpecifyAnnotations(Exclusions.empty()));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();

    assertThat(report)
        .issuesForClassFile("test", "MyJetBrainsClass2")
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
