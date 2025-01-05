package root.scope.methodmarked;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class Prefix1 {

  @NullMarked
  public String addPrefix(String str) {
    return "prefix:" + str;
  }
}
