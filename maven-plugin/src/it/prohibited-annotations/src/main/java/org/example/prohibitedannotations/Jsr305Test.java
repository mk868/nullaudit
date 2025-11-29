package org.example.prohibitedannotations;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public class Jsr305Test {
  @Nullable
  public Object field;

  @Nonnull
  public String method(@Nullable String param) {
    return "test";
  }
}