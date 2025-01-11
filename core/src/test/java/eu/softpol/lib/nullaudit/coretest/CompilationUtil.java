package eu.softpol.lib.nullaudit.coretest;

import com.google.testing.compile.Compilation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CompilationUtil {

  public static List<File> getTestClassPath() {
    return List.of(
        new File(System.getProperty("user.home")
                 + "/.m2/repository/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.jar")
    );
  }

  public static void saveGeneratedFiles(Compilation compilation, Path output) throws IOException {
    var files = compilation.generatedFiles();
    for (var file : files) {
      var relative = file.getName().substring(file.getName().indexOf("/", 1) + 1);
      var path = output.resolve(relative);
      Files.createDirectories(path.getParent());
      try (var os = Files.newOutputStream(path);
          var is = file.openInputStream()) {
        is.transferTo(os);
      }
    }
  }
}
