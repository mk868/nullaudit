def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 3 issues found")
assert log.contains("[ERROR] org.example.unspecified.Joiner#join(java.util.List): Unspecified nullness detected")
assert log.contains("[ERROR] org.example.unspecified.Joiner#Joiner(java.lang.String): Unspecified nullness detected")
assert log.contains("[ERROR] org.example.unspecified.Joiner#delimiter: Unspecified nullness detected")
