# NullAudit

The tool to verify whether nullness annotations are applied to your codebase.

## Features

- Detects UNSPECIFIED nullness types based on `@Nullable`, `@NonNull`, `@NullMarked`, and
  `@NullUnmarked` annotations.
- Analyzes `.jar` files or directories containing `.class` files.
- Generates a JSON report of analysis results.
- Maven plugin to simplify integration with CI/CD workflows.

## Requirements

- **Java 17+**
- **Maven**

## Usage

### Maven plugin

To ensure your code is fully annotated with nullness annotations, add the following plugin
configuration to your `pom.xml`:

```xml
<!-- ... -->
<build>
  <plugins>
    <plugin>
      <groupId>eu.soft-pol.lib.nullaudit</groupId>
      <artifactId>nullaudit-maven-plugin</artifactId>
      <version>1.0-SNAPSHOT</version>
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

When the plugin detects unannotated types, it will fail the build and display the details in the
output.

### Use without Maven project

You can use NullAudit outside of a Maven project.
For example, to find missing nullness annotations in a `.jar` file:

```bash
mvn eu.soft-pol.lib.nullaudit:nullaudit-maven-plugin:1.0-SNAPSHOT:check -Dnullaudit.input=log4j-core-2.24.3.jar
```
