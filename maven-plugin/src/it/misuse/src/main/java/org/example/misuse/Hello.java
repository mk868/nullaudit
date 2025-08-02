package org.example.misuse;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullMarked
@NullUnmarked
public class Hello {

  public void printHello() {
    System.out.println("Hello");
  }

}
