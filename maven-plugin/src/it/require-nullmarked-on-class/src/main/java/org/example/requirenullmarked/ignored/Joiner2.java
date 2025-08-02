package org.example.requirenullmarked.ignored;

import java.util.List;

public class Joiner2 {

  private final String delimiter;

  public Joiner2() {
    this(",");
  }

  public Joiner2(String delimiter) {
    this.delimiter = delimiter;
  }

  public String join(List<String> values) {
    return String.join(delimiter, values);
  }

}
