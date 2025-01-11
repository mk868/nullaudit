package eu.softpol.lib.nullaudit.coretest;

import java.nio.file.Path;

public class Resources {

  public static final Path TEST_CLASSES = Path.of("target", "test-classes");

  public static final Path SAMPLE1_JAR = TEST_CLASSES.resolve("sample1/target/sample1.jar");
  public static final Path SAMPLE1_CLASSES = TEST_CLASSES.resolve("sample1/target/classes");
}
