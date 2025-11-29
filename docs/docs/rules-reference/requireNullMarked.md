# requireNullMarked

**Category**: Rules Reference  
**Scope**: Classes, Interfaces, Enums, Annotations, Packages  
**Purpose**: Ensures that classes or packages are annotated with `@NullMarked` to define a clear
default nullness context.

---

## What does it check?

The `requireNullMarked` rule verifies that classes (and other types like interfaces and enums) or
packages are properly annotated with `@NullMarked`, unless explicitly excluded.

Without `@NullMarked`, types may have **unspecified nullness**, which can lead to ambiguity or bugs.

---

## Why is it important?

- Enforces **explicit nullness context** at the class or package level.
- Helps avoid missing nullability specifications on fields, methods, and parameters.
- Provides a way to set nullness context for entire packages.

---

## Examples

### ❌ Invalid Example: Class without `@NullMarked`

```java
package com.example;

public class UserProfile {

    private String username;
    private String email;
}
```

**Problem detected:**

```
[ERROR] com.example.UserProfile: Missing @NullMarked annotation on class.
```

### ✅ Valid Example: Class with `@NullMarked`

```java
package com.example;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserProfile {

    private String username;
    private String email;
}
```

---

## How to configure it?

You can enable the requireNullMarked rule by adding it to your plugin configuration:

```xml

<configuration>
    <rules>
        <requireNullMarked/>
    </rules>
</configuration>
```

Optional parameters:

| Parameter            | Type     | Default | Description                                                                                                             |
|----------------------|----------|---------|-------------------------------------------------------------------------------------------------------------------------|
| `exclusionsFile`     | `String` | (none)  | Path to a text file listing classes to exclude, see [Exclusions File Format](/docs/file-formats/exclusions-file)        |
| `excludeAnnotations` | `String` | (none)  | Comma-separated list of fully qualified annotation names. Classes with these annotations will be excluded from analysis |
| `on`                 | `String` | `CLASS` | Define where to require putting the `@NullMarked` annotation. Allowed values are `CLASS` or `PACKAGE`                   |

---

## When to enable?

Recommended for:

* New projects starting with JSpecify annotations.
* Projects migrating to explicit nullness declarations.
* Teams that want to enforce consistent annotation practices during development and in CI/CD
  pipelines.
* Projects that prefer package-level nullness declarations (`<on>PACKAGE</on>`).

---

## Limitations

* Only verifies the presence of `@NullMarked`.
* When using `<on>CLASS</on>`, the rule does not require `@NullMarked` on package-info.java — it
  only focuses on types (classes, interfaces, enums, etc.).
* When using `<on>PACKAGE</on>`, all packages must have a package-info.java file with `@NullMarked`.
* Annotation-based exclusions only work at the class level, not for individual members or packages.
