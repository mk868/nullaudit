package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getClassName;
import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getPackageName;
import static eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassUtil.getSimpleClassName;

public record ClassReference(
    String internalName
) {

  public static ClassReference of(String internalName) {
    return new ClassReference(internalName);
  }

  public String packageName() {
    return getPackageName(internalName);
  }

  public String name() {
    return getClassName(internalName);
  }

  public String simpleName() {
    return getSimpleClassName(internalName);
  }

  public String binarySimpleName() {
    String n = name();
    int lastDot = n.lastIndexOf('.');
    return (lastDot < 0) ? n : n.substring(lastDot + 1);
  }
}
