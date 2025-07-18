package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.NAAnnotation;
import java.util.Set;

public interface NAPackage {

  String packageName();

  Set<NAAnnotation> annotations();
}
