package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public final class MutableVisitedMethod implements VisitedMethod {

  private final String methodName;
  private final String descriptiveMethodName;
  private final String methodDescriptor;
  private final @Nullable String methodSignature;
  private final MethodSignature ms;
  private final Set<NullScopeAnnotation> annotations = new HashSet<>();

  public MutableVisitedMethod(String methodName, String descriptiveMethodName,
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
  public MethodSignature ms() {
    return ms;
  }

  @Override
  public Set<NullScopeAnnotation> annotations() {
    return Set.copyOf(annotations);
  }

  public void addAnnotation(NullScopeAnnotation annotation) {
    annotations.add(annotation);
  }
}
