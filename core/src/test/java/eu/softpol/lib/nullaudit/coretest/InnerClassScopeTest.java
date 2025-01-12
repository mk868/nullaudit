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

class InnerClassScopeTest {

  @Test
  void test(@TempDir Path tempDir) throws IOException {
    JavaFileObject inputSource = JavaFileObjects.forSourceString("com.example.Abc", """
        package com.example;
        
        import java.util.List;
        
        import org.jspecify.annotations.NullMarked;
        import org.jspecify.annotations.NullUnmarked;
        
        @NullMarked
        class Abc {
            @NullUnmarked
            class Def {
                record SimpleRecord(
                    String a
                ) {
        
                }
            }
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
    assertThat(summary.totalClasses()).isEqualTo(3);
    assertThat(summary.unspecifiedNullness().classes()).isEqualTo(1);
  }

}
