package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class TypeNode permits CompositeTypeNode, LeafTypeNode {

  private final List<TypeUseAnnotation> annotations = new ArrayList<>();

  protected TypeNode() {
  }

  public void addAnnotation(TypeUseAnnotation annotation) {
    annotations.add(annotation);
  }

  public List<TypeUseAnnotation> getAnnotations() {
    return List.copyOf(annotations);
  }

  public abstract List<TypeNode> getChildren();
}
