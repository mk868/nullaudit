package org.example.unspecified;

import java.util.List;

// Let's assume that someone forgot to apply nullness annotations.
public class Joiner {

  private final String delimiter;

  public Joiner() {
    this(",");
  }

  public Joiner(String delimiter) {
    this.delimiter = delimiter;
  }

  public String join(List<String> values) {
    return String.join(delimiter, values);
  }

}
