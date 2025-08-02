# verifyJSpecifyAnnotations

**Category**: Rules Reference  
**Scope**: Fields, Methods, Type parameters, Classes  
**Purpose**: Ensures that JSpecify annotations are applied correctly and consistently.

---

## What does it check?

The `verifyJSpecifyAnnotations` rule detects **misuses of JSpecify annotations** in your codebase.

Specifically, it validates:

- Conflicting usage of `@NullMarked` and `@NullUnmarked` on the same element.
- Illegal placement of `@Nullable` or `@NonNull` annotations on **primitive types**.

---

## Why is it important?

- Helps ensure the codebase follows **valid JSpecify API usage**.
- Prevents mistakes like annotating primitives (`int`, `boolean`) with nullability annotations,
  which makes no sense and can confuse static analysis tools.
- Ensures that class/method/type-level nullness declarations are **clear and non-conflicting**.

---

## Examples

### ❌ Invalid Example 1: Conflicting annotations

```java
package com.example;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullMarked
class DataHolder {

  @NullMarked
  @NullUnmarked
  void sayHi(String name) {
    System.out.println("Hi " + name);
  }
}
```

**Problem detected:**

```
[ERROR] com.example.DataHolder#sayHi(java.lang.String): Irrelevant annotations, the method should not be annotated with both @NullMarked and @NullUnmarked at the same time!
```

### ❌ Invalid Example 2: Nullable on primitive

```java
package com.example;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DataHolder {

  private @Nullable byte[] bytes;
}
```

**Problem detected:**

```
[ERROR] com.example.DataHolder#bytes: Primitive type cannot be annotated with @Nullable or @NonNull!
```

---

## How to configure it?

You can enable the `verifyJSpecifyAnnotations` rule by adding it in your plugin configuration:

```xml

<configuration>
    <rules>
        <verifyJSpecifyAnnotations/>
    </rules>
</configuration>
```

Optional parameters:

| Parameter            | Type     | Default	 | Description                                                                                                             |
|----------------------|----------|----------|-------------------------------------------------------------------------------------------------------------------------|
| `exclusionsFile`     | `String` | (none)   | Path to a text file listing classes to exclude, see [Exclusions File Format](/docs/file-formats/exclusions-file)        |
| `excludeAnnotations` | `String` | (none)   | Comma-separated list of fully qualified annotation names. Classes with these annotations will be excluded from analysis |

---

## When to enable?

Recommended for:

* Projects already using JSpecify annotations.
* Projects migrating from older nullness standards to JSpecify.
* CI pipelines that want to catch invalid nullness declarations early.

---

## Limitations

* Only checks for structural mistakes (conflicts, invalid targets).
* Annotation-based exclusions only work at the class level, not for individual members or packages.
