package org.example.misuse;

import org.jspecify.annotations.Nullable;

public class DataHolder {

  // A common problem when migrating from JSR305, with JSpecify, it should be: `int @Nullable []`
  private @Nullable int[] data;

}
