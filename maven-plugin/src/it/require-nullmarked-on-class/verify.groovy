def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 1 issue found")
assert log.contains("[ERROR] org.example.requirenullmarked.Joiner: Missing @NullMarked annotation on the class")
