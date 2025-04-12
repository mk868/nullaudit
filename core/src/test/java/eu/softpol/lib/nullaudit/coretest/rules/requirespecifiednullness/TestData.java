package eu.softpol.lib.nullaudit.coretest.rules.requirespecifiednullness;

public class TestData {

  public enum TestAnnotation {
    NONE,
    NULL_MARKED,
    NULL_UNMARKED
  }

  public static String createPackage(String packageName, TestAnnotation packageAnnotation) {
    return """
        %s
        package %s;
        
        %s
        """.formatted(
        switch (packageAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "@NullMarked";
          case NULL_UNMARKED -> "@NullUnmarked";
        },
        packageName,
        switch (packageAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "import org.jspecify.annotations.NullMarked;";
          case NULL_UNMARKED -> "import org.jspecify.annotations.NullUnmarked;";
        }
    );
  }

  public static String createClass(String packageName, String className,
      TestAnnotation classAnnotation) {
    return createClass(packageName, className, classAnnotation, TestAnnotation.NONE);
  }

  public static String createClass(String packageName, String className,
      TestAnnotation classAnnotation, TestAnnotation methodAnnotation) {
    return """
        package %s;
        
        %s
        %s
        
        %s
        public class %s {
        
          public String prefix = "prefix: ";
        
          %s
          public String addPrefix(String str) {
            return prefix + str;
          }
        }
        """.formatted(
        packageName,
        switch (classAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "import org.jspecify.annotations.NullMarked;";
          case NULL_UNMARKED -> "import org.jspecify.annotations.NullUnmarked;";
        },
        switch (methodAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "import org.jspecify.annotations.NullMarked;";
          case NULL_UNMARKED -> "import org.jspecify.annotations.NullUnmarked;";
        },
        switch (classAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "@NullMarked";
          case NULL_UNMARKED -> "@NullUnmarked";
        },
        className,
        switch (methodAnnotation) {
          case NONE -> "";
          case NULL_MARKED -> "@NullMarked";
          case NULL_UNMARKED -> "@NullUnmarked";
        }
    );
  }

}
