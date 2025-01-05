package root.scope.classunmarked;

import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class Prefix1 {

  public String addPrefix(String str) {
    return "prefix:" + str;
  }
}
