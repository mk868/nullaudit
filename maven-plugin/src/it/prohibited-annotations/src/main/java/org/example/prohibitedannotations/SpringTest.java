package org.example.prohibitedannotations;

import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;

public class SpringTest {
  @Nullable
  public Object field;

  @NonNull
  public String method(@Nullable String param) {
    return "test";
  }
}