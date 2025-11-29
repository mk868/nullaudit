package org.example.prohibitedannotations;

import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;

public class JakartaTest {
  @Nullable
  public Object field;

  @Nonnull
  public String method(@Nullable String param) {
    return "test";
  }
}