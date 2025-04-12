How to Use:

```
mvn package
```

This command will produce the following output:

```
...
[ERROR] 3 issues found.
[ERROR] abc.package-info: Irrelevant annotations, the package should not be annotated with both @NullMarked and @NullUnmarked at the same time!
[ERROR] abc.Hello: Irrelevant annotations, the class should not be annotated with both @NullMarked and @NullUnmarked at the same time!
[ERROR] abc.Hello#printHello(): Irrelevant annotations, the method should not be annotated with both @NullMarked and @NullUnmarked at the same time!
...
```
