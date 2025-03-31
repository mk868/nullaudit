package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import java.util.function.BiConsumer;

public interface Check {

  @FunctionalInterface
  interface AddIssueConsumer {

    void accept(String name, List<Kind> kinds, String message);
  }

  default void checkModule() {
  }

  default void checkPackage(
      VisitedPackage visitedPackage,
      BiConsumer<List<Kind>, String> addIssue
  ) {
  }

  default void checkClass(
      VisitedClass visitedClass,
      BiConsumer<List<Kind>, String> addIssue,
      AddIssueConsumer addFieldIssue,
      AddIssueConsumer addMethodIssue
  ) {
  }

}
