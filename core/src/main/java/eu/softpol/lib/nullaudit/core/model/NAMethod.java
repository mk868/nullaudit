package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.immutables.value.Value;

@Value.Immutable
public interface NAMethod {

  default boolean isConstructor() {
    return methodName().equals("<init>");
  }

  String methodName();

  String descriptiveMethodName();

  String methodDescriptor();

  public @Nullable String methodSignature();

  NullScope effectiveNullScope();

  MethodSignature ms();

  Set<NAAnnotation> annotations();

}
