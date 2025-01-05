How to Use:

```
mvn package
```

This command will produce the following output:

```
...
[ERROR] 2 problems found.
[ERROR] abc.Joiner.<init>: Unspecified nullness found:
[ERROR]     void <init>(java.lang.String*)
[ERROR]                                 ^ 
[ERROR] abc.Joiner.join: Unspecified nullness found:
[ERROR]     java.lang.String* join(java.util.List*<java.lang.String*>)
[ERROR]                     ^                    ^                 ^  
...
```
