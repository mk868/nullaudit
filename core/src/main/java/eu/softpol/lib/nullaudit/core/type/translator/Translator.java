package eu.softpol.lib.nullaudit.core.type.translator;

import eu.softpol.lib.nullaudit.core.type.TypeNode;

public interface Translator<T> {

  T translate(TypeNode typeNode);
}
