How to Use:

```
mvn package
```

This command will produce the following output:

```
...
[ERROR] 3 issues found.
[ERROR] org.example.unspecified.Joiner#delimiter: Unspecified nullness found:
[ERROR]     java.lang.String* delimiter
[ERROR]                     ^          
[ERROR] org.example.unspecified.Joiner#Joiner(java.lang.String): Unspecified nullness found:
[ERROR]     void <init>(java.lang.String*)
[ERROR]                                 ^ 
[ERROR] org.example.unspecified.Joiner#join(java.util.List): Unspecified nullness found:
[ERROR]     java.lang.String* join(java.util.List*<java.lang.String*>)
[ERROR]                     ^                    ^                 ^  
...
```
