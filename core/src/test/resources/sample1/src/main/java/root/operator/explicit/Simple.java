package root.operator.explicit;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Simple {

  public boolean isNotBlank(@Nullable String str) {
    return str != null && str.trim().length() > 0;
  }

  public boolean isBlank(@NonNull String str) {
    return str.trim().length() == 0;
  }

  public @NonNull String toUpperCase(@Nullable String str) {
    return str == null ? "" : str.toUpperCase();
  }
}
