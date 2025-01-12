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

class FieldTest {

  @Test
  void test(@TempDir Path tempDir) throws IOException {
    JavaFileObject inputSource = JavaFileObjects.forSourceString("com.example.SimpleClass", """
        package com.example;
        
        import java.util.List;
        
        import org.jspecify.annotations.Nullable;
        
        class SimpleClass {
            List<String> a;
            String b;
            @Nullable List<@Nullable String> c;
        
        }
        """);
    Compilation compilation = Compiler.javac()
        .withClasspath(getTestClassPath())
        .compile(inputSource);
    assertThat(compilation).succeeded();

    saveGeneratedFiles(compilation, tempDir);

    var analyzer = new NullAuditAnalyzer(tempDir, List.of());
    var report = analyzer.run();

    var summary = report.summary();
    assertThat(summary.totalClasses()).isEqualTo(1);
    assertThat(summary.totalFields()).isEqualTo(3);
    assertThat(summary.unspecifiedNullness().classes()).isEqualTo(1);
    assertThat(summary.unspecifiedNullness().fields()).isEqualTo(2);
    var issues = report.issues();
    assertThat(issues).hasSize(2);
    assertThat(issues.get(0).location()).isEqualTo("com.example.SimpleClass#a");
    assertThat(issues.get(0).message()).contains("java.util.List*<java.lang.String*> a");
    assertThat(issues.get(1).location()).isEqualTo("com.example.SimpleClass#b");
    assertThat(issues.get(1).message()).contains("String* b");
  }

}
