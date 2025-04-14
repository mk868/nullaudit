package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import java.util.HashSet;
import java.util.Set;

public final class MutableNAPackage implements NAPackage {

  private final String packageName;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();

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
  public Set<NullScopeAnnotation> annotations() {
    return Set.copyOf(annotations);
  }

  public void addAnnotation(NullScopeAnnotation annotation) {
    annotations.add(annotation);
  }
}
