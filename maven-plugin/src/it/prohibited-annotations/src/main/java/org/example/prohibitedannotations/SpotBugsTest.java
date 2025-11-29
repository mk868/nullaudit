package org.example.prohibitedannotations;

import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.NonNull;

public class SpotBugsTest {
  @Nullable
  public Object field;

  @NonNull
  public String method(@Nullable String param) {
    return "test";
  }
}