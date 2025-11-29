def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 2 issues found")
assert log.contains("[ERROR] org.example.misuse.DataHolder#data: Primitive types cannot be annotated with @Nullable or @NonNull.")
assert log.contains("[ERROR] org.example.misuse.Hello: Conflicting annotations: class should not be annotated with both @NullMarked and @NullUnmarked at the same time.")
