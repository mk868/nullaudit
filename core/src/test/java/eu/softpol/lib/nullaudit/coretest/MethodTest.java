package eu.softpol.lib.nullaudit.coretest;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static eu.softpol.lib.nullaudit.coretest.CompilationUtil.getTestClassPath;
import static eu.softpol.lib.nullaudit.coretest.CompilationUtil.saveGeneratedFiles;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import eu.softpol.lib.nullaudit.core.NullAuditAnalyzer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MethodTest {

  @Test
  void test(@TempDir Path tempDir) throws IOException {
    JavaFileObject complexInputSource = JavaFileObjects.forSourceString("com.example.Complex", """
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
        """);
    JavaFileObject simpleInputSource = JavaFileObjects.forSourceString("com.example.Simple", """
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
        """);
    JavaFileObject invalidInputSource = JavaFileObjects.forSourceString("com.example.Invalid", """
        package com.example;
        
        class Invalid {
        
            public String addPrefix(String str) {
                return "prefix:" + str;
            }
        }
        """);
    Compilation compilation = Compiler.javac()
        .withClasspath(getTestClassPath())
        .compile(complexInputSource, simpleInputSource, invalidInputSource);
    assertThat(compilation).succeeded();

    saveGeneratedFiles(compilation, tempDir);

    var analyzer = new NullAuditAnalyzer(tempDir, List.of());
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
