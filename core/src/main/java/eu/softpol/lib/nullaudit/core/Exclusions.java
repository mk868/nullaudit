package eu.softpol.lib.nullaudit.core;

import java.util.Set;

public record Exclusions(
    Set<String> classes,
    Set<String> annotations
) {

  private static final Exclusions EMPTY = new Exclusions(Set.of(), Set.of());

  public boolean isEmpty() {
    return classes.isEmpty() && annotations.isEmpty();
  }

  public static Exclusions empty() {
    return EMPTY;
  }

  public static Exclusions ofClasses(Set<String> classes) {
    return new Exclusions(Set.copyOf(classes), Set.of());
  }

  public static Exclusions ofClasses(String... classes) {
    return new Exclusions(Set.of(classes), Set.of());
  }
}
