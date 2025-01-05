package eu.softpol.lib.nullaudit.core.source;

import eu.softpol.lib.nullaudit.core.analyzer.FileAnalyzer;
import eu.softpol.lib.nullaudit.core.comparator.CheckOrder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSource implements Source {

  private static final Logger logger = System.getLogger(JarSource.class.getName());

  private final Path jarFile;

  public JarSource(Path jarFile) {
    this.jarFile = jarFile;
  }

  @Override
  public void analyze(FileAnalyzer fileAnalyzer) {
    try (JarFile jar = new JarFile(jarFile.toFile())) {
      // Group entries by their directory structure and traverse recursively
      var entryMap = new HashMap<String, List<JarEntry>>();
      jar.stream().forEach(entry -> {
        if (!entry.isDirectory()) {
          var directory = entry.getName().substring(0, entry.getName().lastIndexOf('/') + 1);
          if (directory.isEmpty()) {
            directory = "/";
          }
          entryMap.computeIfAbsent(directory, k -> new ArrayList<>()).add(entry);
        }
      });

      var dirs = entryMap.keySet().stream()
          .sorted()
          .toList();

      for (var dir : dirs) {
        var files = entryMap.get(dir).stream()
            .sorted(Comparator.comparing(JarSource::getFileName, CheckOrder.COMPARATOR))
            .toList();

        for (var file : files) {
          fileAnalyzer.analyze(getFileName(file), () -> jar.getInputStream(file));
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Error processing jar file: " + jarFile.getFileName(), e);
    }
  }

  private static String getFileName(JarEntry entry) {
    return entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
  }

}
