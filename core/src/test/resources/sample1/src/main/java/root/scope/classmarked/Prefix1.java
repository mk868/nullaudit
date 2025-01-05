package root.scope.classmarked;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class Prefix1 {

  public String addPrefix(String str) {
    return "prefix:" + str;
  }
}
