package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public final class MutableNAMethod implements NAMethod {

  private final String methodName;
  private final String descriptiveMethodName;
  private final String methodDescriptor;
  private final @Nullable String methodSignature;
  private final MethodSignature ms;
  private final Set<NAAnnotation> annotations = new HashSet<>();
  private NullScope effectiveNullScope = NullScope.NOT_DEFINED;

  public MutableNAMethod(String methodName, String descriptiveMethodName,
      String methodDescriptor, @Nullable String methodSignature,
      MethodSignature ms) {
    this.methodName = methodName;
    this.descriptiveMethodName = descriptiveMethodName;
    this.methodDescriptor = methodDescriptor;
    this.methodSignature = methodSignature;
    this.ms = ms;
  }

  @Override
  public String methodName() {
    return methodName;
  }

  @Override
  public String descriptiveMethodName() {
    return descriptiveMethodName;
  }

  @Override
  public String methodDescriptor() {
    return methodDescriptor;
  }

  @Override
  public @Nullable String methodSignature() {
    return methodSignature;
  }

  @Override
  public NullScope effectiveNullScope() {
    return effectiveNullScope;
  }

  public void setEffectiveNullScope(NullScope effectiveNullScope) {
    this.effectiveNullScope = effectiveNullScope;
  }

  @Override
  public MethodSignature ms() {
    return ms;
  }

  @Override
  public Set<NAAnnotation> annotations() {
    return Set.copyOf(annotations);
  }

  public void addAnnotation(NAAnnotation annotation) {
    annotations.add(annotation);
  }
}
