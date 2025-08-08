package eu.softpol.lib.nullaudit.core.model;

import java.util.HashSet;
import java.util.Set;

public final class MutableNAPackage implements NAPackage {

  private final String packageName;
  private final Set<NAAnnotation> annotations = new HashSet<>();

  public MutableNAPackage(
      String packageName
  ) {
    this.packageName = packageName;
  }

  @Override
  public String packageName() {
    return packageName;
  }

  @Override
  public Set<NAAnnotation> annotations() {
    return Set.copyOf(annotations);
  }

  public void addAnnotation(NAAnnotation annotation) {
    annotations.add(annotation);
  }
}
