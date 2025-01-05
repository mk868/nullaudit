package eu.softpol.lib.nullaudit.core.nullnessoperator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SetupProject {

  public static void setup(
      Path dir,
      List<String> includes,
      Path outputDir
  ) {
    try {
      for (var include : includes) {
        var src = dir.resolve(include);
        var dest = outputDir.resolve(include);
        if (Files.isDirectory(src)) {
          copyDirectory(src, dest);
        } else {
          Files.createDirectories(dest.getParent());
          Files.copy(src, dest);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void copyDirectory(Path src, Path dest) throws IOException {
    try (var stream = Files.walk(src)) {
      stream.forEach(sourcePath -> {
        var targetPath = dest.resolve(src.relativize(sourcePath));
        try {
          if (Files.isDirectory(sourcePath)) {
            Files.createDirectories(targetPath);
          } else {
            Files.createDirectories(targetPath.getParent());
            Files.copy(sourcePath, targetPath);
          }
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      });
    }
  }

}
