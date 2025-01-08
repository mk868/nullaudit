package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public record MethodInfo(
    String methodName,
    String descriptiveMethodName,
    String methodDescriptor,
    @Nullable String methodSignature,
    MethodSignature ms, // mutable
    Set<NullScopeAnnotation> annotations // mutable
) {

  public MethodInfo(String methodName, String descriptiveMethodName,
      String methodDescriptor, @Nullable String methodSignature,
      MethodSignature ms) {
    this(methodName, descriptiveMethodName, methodDescriptor, methodSignature, ms, new HashSet<>());
  }
}
