package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MutableVisitedClass implements VisitedClass {

  private final Clazz thisClazz;
  private final Clazz superClazz;
  private final List<VisitedComponent> components = new ArrayList<>();
  private final List<VisitedField> fields = new ArrayList<>();
  private final List<MutableVisitedMethod> methods = new ArrayList<>();
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private NullScope effectiveNullScope = NullScope.NOT_DEFINED;

  public MutableVisitedClass(Clazz thisClazz, Clazz superClazz) {
    this.thisClazz = thisClazz;
    this.superClazz = superClazz;
  }

  @Override
  public Clazz thisClazz() {
    return thisClazz;
  }

  @Override
  public Clazz superClazz() {
    return superClazz;
  }

  @Override
  public NullScope effectiveNullScope() {
    return effectiveNullScope;
  }

  public void setEffectiveNullScope(NullScope nullScope) {
    this.effectiveNullScope = nullScope;
  }

  @Override
  public List<VisitedComponent> components() {
    return List.copyOf(components);
  }

  public void addComponent(VisitedComponent component) {
    components.add(component);
  }

  @Override
  public List<VisitedField> fields() {
    return List.copyOf(fields);
  }

  public void addField(VisitedField field) {
    fields.add(field);
  }

  @Override
  public List<VisitedMethod> methods() {
    return List.copyOf(methods);
  }

  public void addMethod(MutableVisitedMethod method) {
    methods.add(method);
  }

  @Override
  public Set<NullScopeAnnotation> annotations() {
    return annotations;
  }

  public void addAnnotation(NullScopeAnnotation annotation) {
    annotations.add(annotation);
  }

}
