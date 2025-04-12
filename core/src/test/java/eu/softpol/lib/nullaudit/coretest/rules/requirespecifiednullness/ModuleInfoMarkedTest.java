package eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness.TestData.TestAnnotation.NONE;
import static eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness.TestData.TestAnnotation.NULL_MARKED;
import static eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness.TestData.TestAnnotation.NULL_UNMARKED;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ModuleInfoMarkedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathModule("org.example.sample")
          .createFile("module-info.java").withContents("""
              import org.jspecify.annotations.NullMarked;
              
              @NullMarked
              module org.example.sample {
                requires org.jspecify;
              }
              """)
          // unmarked package
          .createFile("packageunmarked/package-info.java")
          .withContents(TestData.createPackage("packageunmarked", NULL_UNMARKED))
          .createFile("packageunmarked/Prefix1.java")
          .withContents(TestData.createClass("packageunmarked", "Prefix1", NONE))
          .createFile("packageunmarked/UnmarkedPrefix1.java")
          .withContents(TestData.createClass("packageunmarked", "UnmarkedPrefix1", NULL_UNMARKED))
          .createFile("packageunmarked/MarkedPrefix1.java")
          .withContents(TestData.createClass("packageunmarked", "MarkedPrefix1", NULL_MARKED))
          .createFile("packageunmarked/PrefixMarkedMethod1.java")
          .withContents(TestData.createClass("packageunmarked", "PrefixMarkedMethod1", NONE, NULL_MARKED))
          .createFile("packageunmarked/MarkedPrefixUnmarkedMethod1.java")
          .withContents(TestData.createClass("packageunmarked", "MarkedPrefixUnmarkedMethod1", NULL_MARKED, NULL_UNMARKED))
          // marked package
          .createFile("packagemarked/package-info.java")
          .withContents(TestData.createPackage("packagemarked", NULL_MARKED))
          .createFile("packagemarked/Prefix1.java")
          .withContents(TestData.createClass("packagemarked", "Prefix1", NONE))
          .createFile("packagemarked/UnmarkedPrefix1.java")
          .withContents(TestData.createClass("packagemarked", "UnmarkedPrefix1", NULL_UNMARKED))
          .createFile("packagemarked/MarkedPrefix1.java")
          .withContents(TestData.createClass("packagemarked", "MarkedPrefix1", NULL_MARKED))
          .createFile("packagemarked/UnmarkedPrefixMarkedMethod1.java")
          .withContents(TestData.createClass("packagemarked", "UnmarkedPrefixMarkedMethod1", NULL_UNMARKED, NULL_MARKED))
          // other
          .createFile("other/Prefix1.java")
          .withContents(TestData.createClass("other", "Prefix1", NONE))
          .createFile("other/UnmarkedPrefix1.java")
          .withContents(TestData.createClass("other", "UnmarkedPrefix1", NULL_UNMARKED))
          .createFile("other/MarkedPrefix1.java")
          .withContents(TestData.createClass("other", "MarkedPrefix1", NULL_MARKED))
          .createFile("other/PrefixUnmarkedMethod1.java")
          .withContents(TestData.createClass("other", "PrefixUnmarkedMethod1", NONE, NULL_UNMARKED))
      ;
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldListMethodsInNullUnmarkedScope() {
    var analyzer = new NullAuditAnalyzer(dir, RequireSpecifiedNullnessConfig.CONFIG);
    var report = analyzer.run();
    assertThat(report).issues()
        .hasSize(12);
    assertThat(report).issuesForMethod("other", "UnmarkedPrefix1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForMethod("other", "PrefixUnmarkedMethod1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForMethod("packagemarked", "UnmarkedPrefix1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForMethod("packageunmarked", "Prefix1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForMethod("packageunmarked", "MarkedPrefixUnmarkedMethod1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForMethod("packageunmarked", "UnmarkedPrefix1", "addPrefix(java.lang.String)").hasSize(1);
    assertThat(report).issuesForField("other", "UnmarkedPrefix1", "prefix").hasSize(1);
    assertThat(report).issuesForField("packagemarked", "UnmarkedPrefix1", "prefix").hasSize(1);
    assertThat(report).issuesForField("packagemarked", "UnmarkedPrefixMarkedMethod1", "prefix").hasSize(1);
    assertThat(report).issuesForField("packageunmarked", "Prefix1", "prefix").hasSize(1);
    assertThat(report).issuesForField("packageunmarked", "PrefixMarkedMethod1", "prefix").hasSize(1);
    assertThat(report).issuesForField("packageunmarked", "UnmarkedPrefix1", "prefix").hasSize(1);
  }

}
