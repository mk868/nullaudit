# prohibitNonJSpecifyAnnotations

**Category**: Rules Reference  
**Scope**: Fields, Methods, Parameters, Record components, Type parameters  
**Purpose**: Enforces the usage of standard JSpecify nullness annotations by prohibiting others.

---

## What does it check?

The `prohibitNonJSpecifyAnnotations` rule scans the bytecode for specific nullness annotations that
come from libraries other than JSpecify.

If it detects any of the following annotations, it reports an
error:

* `javax.annotation.Nullable` / `Nonnull` (JSR-305)
* `jakarta.annotation.Nullable` / `Nonnull`
* `org.jetbrains.annotations.Nullable` / `NotNull`
* `org.eclipse.jdt.annotation.Nullable` / `NonNull`
* `edu.umd.cs.findbugs.annotations.Nullable` / `NonNull`
* `org.checkerframework.checker.nullness.qual.Nullable` / `NonNull`
* `org.springframework.lang.Nullable` / `NonNull`

The rule checks usage on:

* Class/Interface/Record declarations
* Fields
* Method
* Method return type
* Method parameters
* Record components
* Generic type arguments (e.g., `List<@NotNull String>`)

---

## Why is it important?

- **Standardization**: Prevents a mix of different annotation libraries in the same project, which
  can be confusing for developers.
- **Tooling Compatibility**: JSpecify is the standard for nullness in Java. Migrating to it
  ensures better compatibility with modern build tools and IDEs.
- **Clarity**: JSpecify annotations have well-defined semantics regarding nullness, unlike some
  older libraries (e.g., JSR-305) which can be ambiguous (e.g., `javax.annotation.Nullable` vs
  `javax.annotation.CheckForNull`).

---

## Examples

### ❌ Invalid Example: Using JetBrains annotations

```java
import org.jetbrains.annotations.Nullable;

public class UserEntity {

  @Nullable
  private String nickname; // error
}
```

**Problems detected:**

```
[ERROR] UserEntity#nickname: Prohibited nullable annotation found on field: org.jetbrains.annotations.Nullable.
```

### ❌ Invalid Example: Using JSR-305 annotations

```java
import javax.annotation.Nonnull;

public class UserEntity {

  @Nonnull
  public String getName() { // error
    return "Alice";
  }
}
```

**Problems detected:**

```
[ERROR] UserEntity#getName(): Prohibited nullable annotation found on method: javax.annotation.Nonnull.
```

### ✅ Valid Example: Using JSpecify

```java
import org.jspecify.annotations.Nullable;

public class UserEntity {

  @Nullable
  private String nickname;
}
```

---

## How to configure it?

You can enable the `prohibitNonJSpecifyAnnotations` rule by adding it to your plugin configuration:

```xml

<configuration>
  <rules>
    <prohibitNonJSpecifyAnnotations/>
  </rules>
</configuration>
```

Optional parameters:

| Parameter        | Type     | Default	 | Description                                                                                                      |
|------------------|----------|----------|------------------------------------------------------------------------------------------------------------------|
| `exclusionsFile` | `String` | (none)   | Path to a text file listing classes to exclude, see [Exclusions File Format](/docs/file-formats/exclusions-file) |

---

## When to enable?

Recommended for:

* New projects: Start with a clean slate and allow only JSpecify from day one.
* Migration phase: When you decide to switch to JSpecify and want to prevent new "pollution" with
  old annotations while refactoring.

---

## Limitations

* This rule strictly checks for the fully qualified class name (FQCN) of the annotation. It doesn't
  analyze the semantics or meta-annotations.
* It doesn't automatically fix or replace the annotations; it only reports them.
