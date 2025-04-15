package eu.softpol.lib.nullaudit.coretest.rules.requirenullmarked;

import static eu.softpol.lib.nullaudit.coretest.assertions.CustomAssertions.assertThat;
import static io.github.ascopes.jct.assertions.JctAssertions.assertThatCompilation;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import eu.softpol.lib.nullaudit.core.NullAuditConfig;
import eu.softpol.lib.nullaudit.core.NullAuditConfig.RequireNullMarked;
import io.github.ascopes.jct.compilers.JctCompiler;
import io.github.ascopes.jct.compilers.JctCompilers;
import io.github.ascopes.jct.workspaces.Workspaces;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RequireNullMarkedExcludeTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("demo/Prefix1.java")
          .withContents(createClass("demo", "Prefix1"))
          .createFile("demo/Prefix2.java")
          .withContents(createClassWithInnerClass("demo", "Prefix2"))
          .createFile("demo/Prefix3.java")
          .withContents(createClass("demo", "Prefix3"))
          .createFile("demo/foo/Prefix4.java")
          .withContents(createClass("demo.foo", "Prefix4"))
          .createFile("demo/foo/Prefix5.java")
          .withContents(createClassWithInnerClass("demo.foo", "Prefix5"));
      var compilation = compiler.compile(workspace);

      assertThatCompilation(compilation)
          .isSuccessfulWithoutWarnings();
    }
  }

  @Test
  void shouldNotReportExcludedClasses() {
    var config = NullAuditConfig.of()
        .withRequireNullMarked(new RequireNullMarked(Exclusions.of(
            "demo.Prefix1",
            "demo.Prefix2",
            "demo.foo.Prefix4"
        )));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();
    assertThat(report).issues().hasSize(2);
  }

  @Test
  void shouldNotReportExcludedWildcardClasses() {
    var config = NullAuditConfig.of()
        .withRequireNullMarked(new RequireNullMarked(Exclusions.of(
            "demo.*"
        )));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();
    assertThat(report)
        .hasOnlyIssuesForClasses(
            "demo.foo.Prefix4",
            "demo.foo.Prefix5"
        );
  }

  @Test
  void shouldNotReportExcludedWildcardClassesAndSubpackages() {
    var config = NullAuditConfig.of()
        .withRequireNullMarked(new RequireNullMarked(Exclusions.of(
            "demo.**"
        )));
    var analyzer = new NullAuditAnalyzer(dir, config);
    var report = analyzer.run();
    assertThat(report.issues()).isEmpty();
  }

  private static String createClass(String packageName, String className) {
    return """
        package %s;
        
        public class %s {
          public String addPrefix(String str) {
            return "> " + str;
          }
        }
        """.formatted(
        packageName,
        className
    );
  }

  private static String createClassWithInnerClass(String packageName, String className) {
    return """
        package %s;
        
        public class %s {
          Object o = new Object(){
            public String addPrefix(String str) {
              return "> " + str;
            }
          };
        
          public String addPrefix(String str) {
            return "> " + str;
          }
        
          public class Inner {
            Object o = new Object(){
              public String addPrefix(String str) {
                return "> " + str;
              }
            };
        
            public String addPrefix(String str) {
              return "> " + str;
            }
          }
        
          public static class StaticNested {
            Object o = new Object(){
              public String addPrefix(String str) {
                return "> " + str;
              }
            };
        
            public String addPrefix(String str) {
              return "> " + str;
            }
          }
        }
        """.formatted(
        packageName,
        className
    );
  }

}
