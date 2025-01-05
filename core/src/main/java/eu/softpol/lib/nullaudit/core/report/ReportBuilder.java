package eu.softpol.lib.nullaudit.core.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class ReportBuilder {

  List<Problem> problems = new ArrayList<>();

  public void addProblemEntry(@Nullable String moduleName, String className,
      ProblemEntry problemEntry) {
    var problem = problems.stream()
        .filter(p -> Objects.equals(p.module(), moduleName) &&
                     Objects.equals(p.className(), className))
        .findFirst()
        .orElseGet(() -> {
          var p = new Problem(moduleName, className, new ArrayList<>());
          problems.add(p);
          return p;
        });
    problem.entries().add(problemEntry);
  }

  public Report build() {
    return new Report(List.copyOf(problems));
  }
}
