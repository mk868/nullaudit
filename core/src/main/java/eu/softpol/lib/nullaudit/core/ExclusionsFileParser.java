package eu.softpol.lib.nullaudit.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExclusionsFileParser {

  private static final char BOM = '﻿';

  private ExclusionsFileParser() {
  }

  /**
   * Reads patterns from the given file path, ignoring blank lines and comments (#). A leading UTF-8
   * BOM is skipped.
   *
   * @param filePath Path to the file listing excluded classes (or patterns).
   * @return exclusions object
   * @throws IOException in case of IO problems.
   */
  public static Set<String> parse(Path filePath) throws IOException {
    var lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
    if (!lines.isEmpty() && !lines.get(0).isEmpty() && lines.get(0).charAt(0) == BOM) {
      lines.set(0, lines.get(0).substring(1));
    }
    return parseLines(lines);
  }

  /**
   * Reads patterns from the given text, one per line, ignoring blank lines and comments (#). Uses
   * the same format as the exclusions file.
   *
   * @param content Text listing excluded classes (or patterns).
   * @return exclusions object
   */
  public static Set<String> parse(String content) {
    return parseLines(content.lines().toList());
  }

  private static Set<String> parseLines(List<String> lines) {
    Set<String> patterns = new HashSet<>();

    for (String line : lines) {
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
