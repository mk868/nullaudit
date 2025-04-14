package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public final class MutableNAClass implements NAClass {

  private final Clazz thisClazz;
  private final Clazz superClazz;
  private final List<NAComponent> components = new ArrayList<>();
  private final List<NAField> fields = new ArrayList<>();
  private final List<MutableNAMethod> methods = new ArrayList<>();
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();
  private @Nullable Clazz outerClass;
  private NullScope effectiveNullScope = NullScope.NOT_DEFINED;

  public MutableNAClass(Clazz thisClazz, Clazz superClazz) {
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
  public @Nullable Clazz outerClass() {
    return outerClass;
  }

  public void setOuterClass(@Nullable Clazz outerClass) {
    this.outerClass = outerClass;
  }

  @Override
  public NullScope effectiveNullScope() {
    return effectiveNullScope;
  }

  public void setEffectiveNullScope(NullScope nullScope) {
    this.effectiveNullScope = nullScope;
  }

  @Override
  public List<NAComponent> components() {
    return List.copyOf(components);
  }

  public void addComponent(NAComponent component) {
    components.add(component);
  }

  @Override
  public List<NAField> fields() {
    return List.copyOf(fields);
  }

  public void addField(NAField field) {
    fields.add(field);
  }

  @Override
  public List<NAMethod> methods() {
    return List.copyOf(methods);
  }

  public void addMethod(MutableNAMethod method) {
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
