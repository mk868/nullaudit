def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 2 issues found")
assert log.contains("[ERROR] org.example.misuse.DataHolder#data: Primitive type cannot be annotated with @Nullable or @NonNull!")
assert log.contains("[ERROR] org.example.misuse.Hello: Irrelevant annotations, the class should not be annotated with both @NullMarked and @NullUnmarked at the same time!")
