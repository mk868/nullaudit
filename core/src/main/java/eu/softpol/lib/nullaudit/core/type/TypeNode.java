package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class TypeNode permits CompositeTypeNode, LeafTypeNode {

  private final List<TypeUseAnnotation> annotations;

  protected TypeNode(Builder<?> builder) {
    this.annotations = List.copyOf(builder.annotations);
  }

  public List<TypeUseAnnotation> getAnnotations() {
    return List.copyOf(annotations);
  }

  public abstract List<TypeNode> getChildren();

  public abstract Builder<?> toBuilder();

  public abstract static class Builder<T extends Builder<T>> {

    protected final List<TypeUseAnnotation> annotations = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public T addAnnotation(TypeUseAnnotation annotation) {
      this.annotations.add(annotation);
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addAnnotations(List<TypeUseAnnotation> annotation) {
      this.annotations.addAll(annotation);
      return (T) this;
    }

    public abstract TypeNode build();
  }
}
