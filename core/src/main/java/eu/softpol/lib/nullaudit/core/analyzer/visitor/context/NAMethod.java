package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.NAAnnotation;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.Set;
import org.jspecify.annotations.Nullable;

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
