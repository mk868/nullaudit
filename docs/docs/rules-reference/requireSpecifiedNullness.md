# requireSpecifiedNullness

**Category**: Rules Reference  
**Scope**: Fields, Methods, Parameters, Type parameters  
**Purpose**: Ensures that nullness is explicitly specified where required.

---

## What does it check?

The `requireSpecifiedNullness` rule verifies that fields, method parameters, return types, and type
parameters have **explicitly specified nullness**.

Explicit nullness is typically defined by:

- Being under a `@NullMarked` context
- Being individually annotated with `@Nullable` or `@NonNull`

If neither is present, the element is considered to have **unspecified nullness**, and an error is
reported.

---

## Why is it important?

- Promotes **strict null-safety** and removes ambiguities.
- Helps projects enforce JSpecify-compliant code practices.
- Clarifies API contracts and prevents null-related bugs.
- Improves the reliability of static nullness analysis tools.

---

## Examples

### ❌ Invalid Example: Unspecified nullness

```java
package com.example;

public class UserService {

    public String findUsernameById(Integer id) {
        // missing explicit nullness on parameter and return type
        return "user123";
    }
}
```

**Problems detected:**

```
[ERROR] com.example.UserService#findUsernameById(java.lang.Integer): Unspecified nullness found:
[ERROR]     java.lang.String* findUsernameById(java.lang.Integer*)
[ERROR]                     ^                                   ^ 
```

### ✅ Valid Example: Specified nullness

```java
package com.example;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class UserService {

    public String findUsernameById(@Nullable Integer id) {
        return id != null ? "user" + id : "unknown";
    }
}
```

---

## How to configure it?

You can enable the `requireSpecifiedNullness` rule by adding it to your plugin configuration:

```xml

<configuration>
    <rules>
        <requireSpecifiedNullness/>
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

* Projects migrating to JSpecify who want to enforce strict nullness over time.
* New codebases starting from scratch with null-safety first principles.
* CI/CD pipelines to block unspecified nullness in pull requests.

---

## Limitations

* Does not validate runtime behavior — only checks for presence of annotations.
