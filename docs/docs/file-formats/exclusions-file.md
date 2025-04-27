# Exclusions File Format

**Category**: File Formats  
**Purpose**: Defines classes and patterns to be excluded from analysis.

---

## What is the exclusions file?

An **exclusions file** allows you to selectively ignore certain classes from rules (e.g., `requireNullMarked`, `requireSpecifiedNullness`).

It's a simple text file where each line represents a class name or a pattern.

---

## Format rules

- Each non-empty line defines one class or pattern.
- Leading and trailing whitespace is ignored.
- Lines starting with `#` are treated as comments and ignored.
- Wildcards `*` and `**` are supported similarly to Ant-style patterns.

---

## Supported Patterns

| Pattern              | Meaning                                                  |
|----------------------|----------------------------------------------------------|
| `com.example.ModelA` | Exact class match (`ModelA` class in `com.example`)      |
| `com.example.Model*` | Match all classes in `com.example` starting with `Model` |
| `com.example.*`      | Match all classes **only** in `com.example` package      |
| `com.example.**`     | Match all classes in `com.example` and all subpackages   |

---

## Examples

### Example 1: Ignoring specific classes

```text
# Ignore some legacy models
com.example.legacy.LegacyUser
com.example.legacy.LegacyOrder
```

### Example 2: Ignoring an entire package and subpackages

```text
com.example.internal.**
```

Matches:

- `com.example.internal.Helper`
- `com.example.internal.utils.Mapper`
- `com.example.internal.subpackage.ModelX`

---

### Example 3: Ignoring a class name prefix

```text
com.example.model.Model*
```

Matches:

- `com.example.model.ModelUser`
- `com.example.model.ModelProduct`
- But **not** `com.example.model.UserModel`

---

## Best Practices

- Prefer using explicit class names for critical exclusions.
- Use `**` only when you are sure you want to exclude entire packages recursively.
- Keep exclusions files under version control to track excluded files.
- Avoid overusing exclusions, especially for new code.

---

## Limitations

- Matching is case-sensitive.
- Only FQCN (Fully Qualified Class Names) are supported — not file paths.
- Inner classes are excluded together with their outer class.

