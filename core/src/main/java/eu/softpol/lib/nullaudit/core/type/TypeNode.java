package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class TypeNode permits CompositeTypeNode, LeafTypeNode {

  private final List<NAAnnotation> annotations;

  protected TypeNode(Builder<?> builder) {
    this.annotations = List.copyOf(builder.annotations);
  }

  public List<NAAnnotation> getAnnotations() {
    return List.copyOf(annotations);
  }

  public abstract List<TypeNode> getChildren();

  public abstract Builder<?> toBuilder();

  public abstract static class Builder<T extends Builder<T>> {

    protected final List<NAAnnotation> annotations = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public T addAnnotation(NAAnnotation annotation) {
      this.annotations.add(annotation);
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addAnnotations(List<NAAnnotation> annotation) {
      this.annotations.addAll(annotation);
      return (T) this;
    }

    public abstract TypeNode build();
  }
}
