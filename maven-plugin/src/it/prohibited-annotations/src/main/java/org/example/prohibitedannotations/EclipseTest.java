package org.example.prohibitedannotations;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNull;

public class EclipseTest {
  public @Nullable Object field;

  public @NonNull String method(@Nullable String param) {
    return "test";
  }
}