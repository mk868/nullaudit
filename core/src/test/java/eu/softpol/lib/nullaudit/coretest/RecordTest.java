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

class RecordTest {

  @Test
  void test(@TempDir Path tempDir) throws IOException {
    JavaFileObject inputSource = JavaFileObjects.forSourceString("com.example.SimpleRecord", """
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
    Compilation compilation = Compiler.javac()
        .withClasspath(getTestClassPath())
        .compile(inputSource);
    assertThat(compilation).succeeded();

    saveGeneratedFiles(compilation, tempDir);

    var analyzer = new NullAuditAnalyzer(tempDir, List.of());
    var report = analyzer.run();

    assertThat(report.issues()).hasSize(2);
    assertThat(report.issues().get(0).message()).contains("java.util.List*<java.lang.String*> a");
    assertThat(report.issues().get(1).message()).contains("String* b");
  }

}
