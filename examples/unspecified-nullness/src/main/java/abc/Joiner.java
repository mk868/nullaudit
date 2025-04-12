package abc;

import java.util.List;

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
