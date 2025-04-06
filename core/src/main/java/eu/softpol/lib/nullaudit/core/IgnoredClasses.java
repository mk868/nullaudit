package eu.softpol.lib.nullaudit.core;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.Clazz;
import java.util.Set;

public record IgnoredClasses(
    Set<String> classes
) {

  /**
   * @param clazz Clazz
   * @return true if class matches any pattern, false otherwise.
   */
  public boolean isIgnored(Clazz clazz) {
    var fqcn = clazz.name();
    // Basic approach: if pattern ends with '*', treat it as a package prefix
    for (String pattern : classes) {
      if (pattern.endsWith("*")) {
        // e.g. pattern = com.example.*
        String prefix = pattern.substring(0, pattern.length() - 1); // drop '*'
        if (fqcn.startsWith(prefix)) {
          return true;
        }
      } else {
        // exact match
        if (fqcn.equals(pattern)) {
          return true;
        }
      }
    }
    return false;
  }
}
