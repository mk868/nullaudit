How to Use:

```
mvn package
```

This command will produce the following output:

```
...
[ERROR] 2 issues found.
[ERROR] org.example.misuse.DataHolder#data: Primitive type cannot be annotated with @Nullable or @NonNull!
[ERROR] org.example.misuse.Hello: Irrelevant annotations, the class should not be annotated with both @NullMarked and @NullUnmarked at the same time!
...
```
