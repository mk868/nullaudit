package eu.softpol.lib.nullaudit.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ExclusionsFileParser {

  private ExclusionsFileParser() {
  }

  /**
   * Reads patterns from the given file path, ignoring blank lines and comments (#).
   *
   * @param filePath Path to the file listing excluded classes (or patterns).
   * @return exclusions object
   * @throws IOException in case of IO problems.
   */
  public static Set<String> parse(Path filePath) throws IOException {
    Set<String> patterns = new HashSet<>();

    for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
      String trimmed = line.trim();
      // skip comments and blank lines
      if (trimmed.isEmpty() || trimmed.startsWith("#")) {
        continue;
      }
      patterns.add(trimmed);
    }

    return Set.copyOf(patterns);
  }


}
