package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassReference;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public final class MutableNAClass implements NAClass {

  private final ClassReference thisClass;
  private final ClassReference superClass;
  private final List<NAComponent> components = new ArrayList<>();
  private final List<NAField> fields = new ArrayList<>();
  private final List<MutableNAMethod> methods = new ArrayList<>();
  private final Set<KnownAnnotations> annotations = new HashSet<>();
  private @Nullable ClassReference topClass;
  private @Nullable ClassReference outerClass;
  private NullScope effectiveNullScope = NullScope.NOT_DEFINED;

  public MutableNAClass(ClassReference thisClass, ClassReference superClass) {
    this.thisClass = thisClass;
    this.superClass = superClass;
  }

  @Override
  public ClassReference thisClazz() {
    return thisClass;
  }

  @Override
  public ClassReference superClazz() {
    return superClass;
  }

  @Override
  public ClassReference topClass() {
    return topClass;
  }

  public void setTopClazz(ClassReference topClass) {
    this.topClass = topClass;
  }

  @Override
  public @Nullable ClassReference outerClass() {
    return outerClass;
  }

  public void setOuterClass(@Nullable ClassReference outerClass) {
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
  public Set<KnownAnnotations> annotations() {
    return annotations;
  }

  public void addAnnotation(KnownAnnotations annotation) {
    annotations.add(annotation);
  }

}
