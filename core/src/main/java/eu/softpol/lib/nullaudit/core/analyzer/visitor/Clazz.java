package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getClassName;
import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;
import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getSimpleClassName;

public record Clazz(
    String internalName,
    String packageName,
    String name,
    String simpleName
) {

  public static Clazz of(String internalName) {
    return new Clazz(
        internalName,
        getPackageName(internalName),
        getClassName(internalName),
        getSimpleClassName(internalName)
    );
  }
}
