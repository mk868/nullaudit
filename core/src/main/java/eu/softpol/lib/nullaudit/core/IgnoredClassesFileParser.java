package eu.softpol.lib.nullaudit.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class IgnoredClassesFileParser {

  private IgnoredClassesFileParser() {
  }

  /**
   * Reads patterns from the given file path, ignoring blank lines and comments (#).
   *
   * @param filePath Path to the file listing ignored classes (or patterns).
   * @return ignored classes object
   * @throws IOException in case of IO problems.
   */
  public static IgnoredClasses parseIgnoredClassesFile(Path filePath) throws IOException {
    Set<String> patterns = new HashSet<>();

    for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
      String trimmed = line.trim();
      // skip comments and blank lines
      if (trimmed.isEmpty() || trimmed.startsWith("#")) {
        continue;
      }
      patterns.add(trimmed);
    }

    return new IgnoredClasses(Set.copyOf(patterns));
  }


}
