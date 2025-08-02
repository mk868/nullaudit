package org.example.requirenullmarked;

import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Splitter {

  private final String delimiter;

  public Splitter() {
    this(",");
  }

  public Splitter(String delimiter) {
    this.delimiter = delimiter;
  }

  public List<String> split(String value) {
    return Arrays.stream(value.split(delimiter)).toList();
  }

}
