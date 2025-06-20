package eu.softpol.lib.nullaudit.core.analyzer.visitor.context;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.KnownAnnotations;
import java.util.Set;

public interface NAPackage {

  String packageName();

  Set<KnownAnnotations> annotations();
}
