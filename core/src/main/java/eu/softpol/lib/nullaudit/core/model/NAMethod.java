package eu.softpol.lib.nullaudit.core.model;

import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.Set;
import org.immutables.value.Value;
import org.jspecify.annotations.Nullable;

@Value.Immutable
public interface NAMethod {

  default boolean isConstructor() {
    return methodName().equals("<init>");
  }

  String methodName();

  String descriptiveMethodName();

  String methodDescriptor();

  @Nullable String methodSignature();

  MethodSignature ms();

  Set<NAAnnotation> annotations();

}
