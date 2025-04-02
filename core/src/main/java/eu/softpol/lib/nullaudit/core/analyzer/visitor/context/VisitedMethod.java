package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.signature.MethodSignature;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface VisitedMethod {

  default boolean isConstructor() {
    return methodName().equals("<init>");
  }

  String methodName();

  String descriptiveMethodName();

  String methodDescriptor();

  public @Nullable String methodSignature();

  MethodSignature ms();

  Set<NullScopeAnnotation> annotations();

}
