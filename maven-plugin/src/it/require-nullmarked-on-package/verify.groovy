def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 3 issues found")
assert log.contains("[ERROR] org.example.requirenullmarked.package-info: Missing @NullMarked annotation on the package")
assert log.contains("[ERROR] org.example.requirenullmarked.p2.package-info: Missing @NullMarked annotation on the package")
assert log.contains("[ERROR] org.example.requirenullmarked.p3.package-info: Missing @NullMarked annotation on the package")
