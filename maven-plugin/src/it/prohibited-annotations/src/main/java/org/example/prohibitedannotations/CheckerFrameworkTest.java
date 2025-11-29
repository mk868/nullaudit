package org.example.prohibitedannotations;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CheckerFrameworkTest {

  public @Nullable Object field;

  public @NonNull String method(@Nullable String param) {
    return "test";
  }
}