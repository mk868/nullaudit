package eu.softpol.lib.nullaudit.core.source;

import eu.softpol.lib.nullaudit.core.analyzer.FileAnalyzer;
import eu.softpol.lib.nullaudit.core.comparator.CheckOrder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class DirSource implements Source {

  private final Path directory;

  public DirSource(Path directory) {
    this.directory = directory;
  }

  @Override
  public void analyze(FileAnalyzer fileAnalyzer) {
    var queue = new LinkedHashSet<Path>();
    queue.add(directory);

    while (!queue.isEmpty()) {
      var dir = queue.stream().findFirst().get();
      queue.remove(dir);

      File[] files = dir.toFile().listFiles();
      if (files == null) {
        continue;
      }

      var sortedFiles = Arrays.stream(files)
          .sorted(Comparator.comparing(File::getName, CheckOrder.COMPARATOR))
          .toList();

      for (File file : sortedFiles) {
        if (file.isDirectory()) {
          queue.add(file.toPath());
        } else {
          fileAnalyzer.analyze(file.getName(), () -> Files.newInputStream(file.toPath()));
        }
      }
    }
  }

}
