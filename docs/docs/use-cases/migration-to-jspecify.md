# Migration to JSpecify Annotations

**Category**: Typical Use Cases  
**Goal**: Assist gradual migration of legacy codebases to proper JSpecify nullness annotations.

---

## Why migrate to JSpecify?

[JSpecify](https://jspecify.dev) offers a **standardized** and **future-proof** way to declare
nullness expectations in Java code.  
Migrating your project brings benefits like:

- Clearer contracts for APIs (parameters, fields, return types).
- Safer static analysis and better compiler support.
- Improved interoperability with future tooling ecosystems.
- Consistent practices across teams and projects.

---

## Challenges During Migration

When dealing with an existing codebase, you will likely encounter:

- Classes with no nullness annotations at all.
- Inconsistent use of old annotations (e.g., from `javax.annotation`, `org.jetbrains`, or custom
  ones).
- Difficulties in manually reviewing thousands of fields, methods, and parameters.

---

## How NullAudit Helps

NullAudit provides automation to **guide and enforce** your migration process:

- Detects classes missing `@NullMarked`.
- Detects fields, parameters, and return types with unspecified nullness.
- Helps you track migration progress over time.
- Allows exclusions for legacy classes that you don't want to migrate immediately.

---

## Typical Migration Workflow

### 1. Set up NullAudit with key rules

Add the plugin to your `pom.xml` and configure:

```xml

<configuration>
    <rules>
        <requireNullMarked>
            <exclusionsFile>legacy-classes.txt</exclusionsFile>
        </requireNullMarked>
        <requireSpecifiedNullness>
            <exclusionsFile>legacy-classes.txt</exclusionsFile>
        </requireSpecifiedNullness>
        <prohibitNonJSpecifyAnnotations>
            <exclusionsFile>legacy-classes.txt</exclusionsFile>
        </prohibitNonJSpecifyAnnotations>
    </rules>
</configuration>
```

---

## 2. Create an exclusions file

List all classes you want to temporarily skip during migration:

```
com.example.legacy.LegacyUser
com.example.internal.**
```

This allows you to focus on new code first, while gradually cleaning up the old parts.

:::tip

You can generate a file with all existing classes using the command:

```
find ./src/main/java -name "*.java" | sed 's|./src/main/java/||' | sed 's|/|.|g' | sed 's|.java$||' > legacy-classes.txt
```

:::


---

## 3. Enforce rules on new code

For each new class — not listed in the `legacy-classes.txt` file:

* Make sure to add the `@NullMarked` on the class.
* Explicitly annotate fields, parameters, and return types with `@Nullable` when needed.
* **Do not use legacy annotations** (e.g., `javax.annotation.Nullable`, `org.jetbrains.annotations.NotNull`). The `prohibitNonJSpecifyAnnotations` rule will detect and block them.

---

## 4. Tighten the rules over time

As you migrate more parts of your codebase:

1. Pick a package or a set of classes from `legacy-classes.txt`.
2. Remove them from the file.
3. Run `mvn compile` (NullAudit will report errors).
4. Fix annotations in those classes.
5. Commit changes.

---

## Example: Before and After

Before migration:

```java
package com.example.service;

public class UserService {

    public String findUsernameById(Integer id) {
        return "user" + id;
    }
}
```

Problems:

* No `@NullMarked` on class or package.
* Parameter `id` has unspecified nullness.
* Return type `String` has unspecified nullness.

After applying JSpecify annotations:

```java
package com.example.service;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class UserService {

    public String findUsernameById(@Nullable Integer id) {
        return id != null ? "user" + id : "unknown";
    }
}
```

Now:

* The class has a clear nullness context.
* Parameter `id` is explicitly declared nullable.
* Return type is non-null by default (implied by `@NullMarked`).

---

## Best Practices for Migration

* Start with new code: require nullness on all new classes and PRs.
* Annotate critical APIs and public interfaces first.
* Use exclusions only as a temporary workaround — not permanently.
* Review and update legacy code in manageable increments.
* Integrate NullAudit checks into your CI/CD pipelines early.
