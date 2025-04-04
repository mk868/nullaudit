# NullAudit

A tool to verify whether JSpecify nullness annotations are applied to your codebase.  
Check out the [sample projects](examples) for the examples of actual usage.

## Checks

- Detects unspecified nullness types based on `@Nullable`, `@NonNull`, `@NullMarked`, and
  `@NullUnmarked` annotations.
  ```java
  public class SayHello {
    public void say(String message) {
      System.out.println(message);
    }
  }
  ```
  Gives:
  ```
  SayHello#say(java.lang.String): Unspecified nullness found:
  void say(java.lang.String*)
                           ^
  ```
- Detects the use of both `@NullMarked` and
  `@NullUnmarked` annotations on the same package/class/method and reports it as an error.
  ```java
  @NullMarked
  @NullUnmarked
  class SampleClass {
  }
  ```
  Gives:
  ```
  SampleClass: Irrelevant annotations, the class should not be annotated with both @NullMarked and @NullUnmarked at the same time!
  ```
- Detects unexpected annotations on the primitive types.
  ```java
  @NullMarked
  class DataHolder {
    private @Nullable byte[] data;
  }
  ```
  Gives:
  ```
  DataHolder#data: Primitive type cannot be annotated with @Nullable or @NonNull!
  ```

## Features

- Analyzes `.jar` files or directories containing `.class` files.
- Generates a JSON report of the analysis results.
- Maven plugin to simplify integration with CI/CD workflows.

## Requirements

- Java 17+
- Maven

## Usage

### Maven Plugin

The plugin provides two goals:

- **`check`** - Analyzes the codebase to detect types with an unspecified nullness value.  
  If any are found, the execution fails and displays the details in the output.
- **`report`** - Generates a JSON report containing details about places in the code where
  nullness is unspecified.

#### Usage in `pom.xml`

To ensure your code is fully annotated with nullness annotations, add the following plugin
configuration to your `pom.xml`:

```xml
<!-- ... -->
<build>
  <plugins>
    <plugin>
      <groupId>eu.soft-pol.lib.nullaudit</groupId>
      <artifactId>nullaudit-maven-plugin</artifactId>
      <version>0.3.0</version>
      <configuration>
        <!-- Limit the number of issues displayed on the console -->
        <maxErrors>100</maxErrors>
      </configuration>
      <executions>
        <execution>
          <phase>compile</phase>
          <goals>
            <goal>check</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
  <!-- ... -->
```

#### Usage as a standalone tool

You can also use NullAudit outside of a Maven project.  
For example, to find unspecified nullness in a `.jar` file, run:

```bash
mvn eu.soft-pol.lib.nullaudit:nullaudit-maven-plugin:0.3.0:check -Dnullaudit.input=log4j-core-2.24.3.jar
```

To generate a JSON report for a `.jar` file, run:

```bash
mvn eu.soft-pol.lib.nullaudit:nullaudit-maven-plugin:0.3.0:report -Dnullaudit.input=log4j-core-2.24.3.jar -Dnullaudit.reportFile=report.json
```
