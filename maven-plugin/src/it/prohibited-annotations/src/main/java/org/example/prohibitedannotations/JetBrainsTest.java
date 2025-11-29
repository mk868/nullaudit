package org.example.prohibitedannotations;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public class JetBrainsTest {
  @Nullable
  public Object field;

  @NotNull
  public String method(@Nullable String param) {
    return "test";
  }
}