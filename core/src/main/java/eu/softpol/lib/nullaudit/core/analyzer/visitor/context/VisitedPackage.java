package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import java.util.Set;

public interface VisitedPackage {

  String packageName();

  Set<NullScopeAnnotation> annotations();
}
