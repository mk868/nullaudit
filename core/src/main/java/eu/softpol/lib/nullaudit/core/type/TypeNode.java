package eu.softpol.lib.nullaudit.core.type;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class TypeNode permits ArrayTypeNode, PrimitiveTypeNode, ClassTypeNode,
    UnboundedTypeNode, VariableTypeNode {

  private final List<TypeUseAnnotation> annotations = new ArrayList<>();

  protected TypeNode() {
  }

  protected abstract void addChild(TypeNode child);

  public TypeNode addClassChild(String value) {
    var child = new ClassTypeNode(value);
    addChild(child);
    return child;
  }

  public TypeNode addPrimitiveChild(char descriptor) {
    var child = new PrimitiveTypeNode(descriptor);
    addChild(child);
    return child;
  }

  public TypeNode addArrayChild() {
    var child = new ArrayTypeNode();
    addChild(child);
    return child;
  }

  public TypeNode addVariableChild(String name) {
    var child = new VariableTypeNode(name);
    addChild(child);
    return child;
  }

  public TypeNode addUnboundedChild() {
    var child = new UnboundedTypeNode();
    addChild(child);
    return child;
  }

  public void addAnnotation(TypeUseAnnotation annotation) {
    annotations.add(annotation);
  }

  public List<TypeUseAnnotation> getAnnotations() {
    return annotations;
  }

  public abstract List<TypeNode> getChildren();
}
