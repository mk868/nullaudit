package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import java.util.HashSet;
import java.util.Set;

public final class MutableNAPackage implements NAPackage {

  private final String packageName;
  private final Set<KnownAnnotations> annotations = new HashSet<>();

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
  public Set<KnownAnnotations> annotations() {
    return Set.copyOf(annotations);
  }

  public void addAnnotation(KnownAnnotations annotation) {
    annotations.add(annotation);
  }
}
