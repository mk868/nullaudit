How to Use:

```
mvn package
```

This command will produce the following output:

```
...
[ERROR] 2 issues found.
[ERROR] abc.Joiner#Joiner(java.lang.String): Unspecified nullness found:
[ERROR]     void <init>(java.lang.String*)
[ERROR]                                 ^ 
[ERROR] abc.Joiner#join(java.util.List): Unspecified nullness found:
[ERROR]     java.lang.String* join(java.util.List*<java.lang.String*>)
[ERROR]                     ^                    ^                 ^  
...
```
