package root.scope.innerclassunmarked;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullMarked
public class Prefix1 {

  @NullUnmarked
  public class Inner {

    public String addPrefix(String str) {
      return "prefix:" + str;
    }

  }
}
