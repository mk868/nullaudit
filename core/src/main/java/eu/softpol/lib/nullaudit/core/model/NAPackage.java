package eu.softpol.lib.nullaudit.core.model;

import java.util.Set;

public interface NAPackage {

  String packageName();

  Set<NAAnnotation> annotations();
}
