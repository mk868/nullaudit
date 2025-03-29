package eu.softpol.lib.nullaudit.core.analyzer.visitor;

public class ClassUtil {

  private ClassUtil() {
  }

  /**
   * Get package for specified internal class name. For example for the input class
   * `aaa/bbb/ClassAbc` a package `aaa.bbb` will be returned.
   *
   * @param name internal class name
   * @return package name
   */
  public static String getPackageName(String name) {
    int lastSlash = name.lastIndexOf("/");
    if (lastSlash == -1) {
      return "";
    } else {
      return name.substring(0, lastSlash).replace("/", ".");
    }
  }

  /**
   * Get class name for specified internal class name. For example for the input class
   * `aaa/bbb/ClassAbc` a `aaa.bbb.ClassAbc` will be returned.
   *
   * @param name internal class name
   * @return class name
   */
  public static String getClassName(String name) {
    return name.replace("/", ".");
  }

  /**
   * Get class name for specified descriptor. For example for the input
   * `Lorg/jspecify/annotations/Nullable;` a `org.jspecify.annotations.Nullable` will be returned.
   *
   * @param descriptor class descriptor
   * @return class name
   */
  public static String getClassNameFromDescriptor(String descriptor) {
    return descriptor.substring(1, descriptor.length() - 1)
        .replace("/", ".");
  }

  /**
   * Get simple class name for specified internal class name. For example for the input class
   * `aaa/bbb/ClassAbc$Def` a `Def` will be returned.
   *
   * @param name internal class name
   * @return simple class name
   */
  public static String getSimpleClassName(String name) {
    var className = getClassName(name);
    if (className.contains(".")) {
      className = className.substring(className.lastIndexOf(".") + 1);
    }
    if (className.contains("$")) {
      className = className.substring(className.lastIndexOf("$") + 1);
    }
    return className;
  }

}
