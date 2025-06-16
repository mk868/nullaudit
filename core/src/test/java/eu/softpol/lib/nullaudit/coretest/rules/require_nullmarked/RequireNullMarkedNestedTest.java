package eu.softpol.lib.nullaudit.coretest.rules.require_nullmarked;

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

class RequireNullMarkedNestedTest {

  @TempDir
  Path dir;

  @BeforeEach
  void compile() {
    JctCompiler compiler = JctCompilers.newPlatformCompiler();
    try (var workspace = Workspaces.newWorkspace()) {
      workspace.addClassOutputPackage(dir);
      workspace
          .createSourcePathPackage()
          .createFile("marked/OuterClass.java").withContents("""
              package marked;
              
              import java.util.function.Consumer;
              import org.jspecify.annotations.NullMarked;
              
              @NullMarked
              public class OuterClass {
              
                public void hello() {
                }
              
                public static final Consumer<String> SAY_HELLO = new Consumer<>() {
                  public void accept(String name) {
                    System.out.println("Hello " + name);
                  }
                };
              
                public static class StaticNestedClass {
                  public static final Consumer<String> SAY_HI = new Consumer<>() {
                    public void accept(String name) {
                      System.out.println("Hi " + name);
                    }
                  };
              
                  public void sayHi(String name) {
                    SAY_HI.accept(name);
                  }
                }
              
                public class InnerClass {
                  public static final Consumer<String> SAY_HI = new Consumer<>() {
                    public void accept(String name) {
                      System.out.println("Hi " + name);
                    }
                  };
              
                  public void sayHi(String name) {
                    SAY_HI.accept(name);
                  }
                }
              }
              """)
          .createFile("unmarked/OuterClass.java").withContents("""
              package unmarked;
              
              import java.util.function.Consumer;
              
              public class OuterClass {
              
                public void hello() {
                }
              
                public static final Consumer<String> SAY_HELLO = new Consumer<>() {
                  public void accept(String name) {
                    System.out.println("Hello " + name);
                  }
                };
              
                public static class StaticNestedClass {
                  public static final Consumer<String> SAY_HI = new Consumer<>() {
                    public void accept(String name) {
                      System.out.println("Hi " + name);
                    }
                  };
              
                  public void sayHi(String name) {
                    SAY_HI.accept(name);
                  }
                }
              
                public class InnerClass {
                  public static final Consumer<String> SAY_HI = new Consumer<>() {
                    public void accept(String name) {
                      System.out.println("Hi " + name);
                    }
                  };
              
                  public void sayHi(String name) {
                    SAY_HI.accept(name);
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
  void shouldReportIssueOnlyForOuterClass() {
    var analyzer = new NullAuditAnalyzer(dir, RulesConfig.REQUIRE_NULLMARKED);
    var report = analyzer.run();
    assertThat(report).issues().hasSize(1);
    assertThat(report).issuesForClass("unmarked", "OuterClass").hasSize(1);
  }

}
