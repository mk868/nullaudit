def logFile = new File(basedir, "build.log")
def log = logFile.text

assert log.contains("[ERROR] 14 issues found")

// Jakarta
assert log.contains("[ERROR] org.example.prohibitedannotations.JakartaTest#method(java.lang.String): Prohibited nullable annotation found on method: jakarta.annotation.Nonnull, jakarta.annotation.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.JakartaTest#field: Prohibited nullable annotation found on field: jakarta.annotation.Nullable.")

// JSR-305
assert log.contains("[ERROR] org.example.prohibitedannotations.Jsr305Test#method(java.lang.String): Prohibited nullable annotation found on method: javax.annotation.Nonnull, javax.annotation.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.Jsr305Test#field: Prohibited nullable annotation found on field: javax.annotation.Nullable.")

// Eclipse JDT
assert log.contains("[ERROR] org.example.prohibitedannotations.EclipseTest#field: Prohibited nullable annotation found on field: org.eclipse.jdt.annotation.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.EclipseTest#method(java.lang.String): Prohibited nullable annotation found on method: org.eclipse.jdt.annotation.NonNull, org.eclipse.jdt.annotation.Nullable.")

// Checker Framework
assert log.contains("[ERROR] org.example.prohibitedannotations.CheckerFrameworkTest#method(java.lang.String): Prohibited nullable annotation found on method: org.checkerframework.checker.nullness.qual.NonNull, org.checkerframework.checker.nullness.qual.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.CheckerFrameworkTest#field: Prohibited nullable annotation found on field: org.checkerframework.checker.nullness.qual.Nullable.")

// JetBrains
assert log.contains("[ERROR] org.example.prohibitedannotations.JetBrainsTest#field: Prohibited nullable annotation found on field: org.jetbrains.annotations.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.JetBrainsTest#method(java.lang.String): Prohibited nullable annotation found on method: org.jetbrains.annotations.NotNull, org.jetbrains.annotations.Nullable.")

// SpotBugs
assert log.contains("[ERROR] org.example.prohibitedannotations.SpotBugsTest#method(java.lang.String): Prohibited nullable annotation found on method: edu.umd.cs.findbugs.annotations.NonNull, edu.umd.cs.findbugs.annotations.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.SpotBugsTest#field: Prohibited nullable annotation found on field: edu.umd.cs.findbugs.annotations.Nullable.")

// Spring Core
assert log.contains("[ERROR] org.example.prohibitedannotations.SpringTest#method(java.lang.String): Prohibited nullable annotation found on method: org.springframework.lang.NonNull, org.springframework.lang.Nullable.")
assert log.contains("[ERROR] org.example.prohibitedannotations.SpringTest#field: Prohibited nullable annotation found on field: org.springframework.lang.Nullable.")
