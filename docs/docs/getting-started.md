---
sidebar_position: 1
---

# Quick Start

Get up and running with **NullAudit** in just a few steps!

NullAudit is a static analyzer that supports the usage of [JSpecify](https://jspecify.dev)
nullness annotations in Java codebases. It is designed to be easy to integrate into Maven projects.

---

## 1. Add the Maven Plugin

In your project's `pom.xml`, add the following configuration:

```xml

<plugin>
    <groupId>eu.soft-pol.lib.nullaudit</groupId>
    <artifactId>nullaudit-maven-plugin</artifactId>
    <version>0.7.0</version>
    <configuration>
        <rules>
            <requireNullMarked/>
            <requireSpecifiedNullness/>
            <verifyJSpecifyAnnotations/>
            <prohibitNonJSpecifyAnnotations/>
        </rules>
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
```

:::tip

You can configure only the rules you need!  
For example, to only verify JSpecify usage:

```xml

<configuration>
    <rules>
        <verifyJSpecifyAnnotations/>
    </rules>
</configuration>
```

:::

---

## 2. Build the Project

After configuring the plugin, simply run:

```
mvn compile
```

During the build, NullAudit will scan your Java classes and validate their nullness annotations.

If any issues are found:

* They will be printed in the console (up to a configurable limit).

---
