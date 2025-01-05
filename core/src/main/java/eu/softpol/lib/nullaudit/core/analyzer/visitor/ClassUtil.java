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
   * @return package name
   */
  public static String getClassName(String name) {
    return name.replace("/", ".");
  }

}
