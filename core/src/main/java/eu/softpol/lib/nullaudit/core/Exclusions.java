package eu.softpol.lib.nullaudit.core;

import java.util.Set;

public record Exclusions(
    Set<String> classes
) {

  private static final Exclusions EMPTY = new Exclusions(Set.of());

  public boolean isEmpty() {
    return classes.isEmpty();
  }

  public static Exclusions empty() {
    return EMPTY;
  }

  public static Exclusions of(Set<String> classes) {
    return new Exclusions(Set.copyOf(classes));
  }

  public static Exclusions of(String... classes) {
    return new Exclusions(Set.of(classes));
  }
}
