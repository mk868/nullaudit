package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import java.util.function.BiConsumer;

public interface Check {

  interface AddIssue {

    void addIssueForClass(List<Kind> kinds, String message);

    void addIssueForField(String name, List<Kind> kinds, String message);

    void addIssueForMethod(String name, List<Kind> kinds, String message);
  }

  void checkModule();

  void checkPackage(VisitedPackage visitedPackage, BiConsumer<List<Kind>, String> addIssue);

  void checkClass(VisitedClass visitedClass, AddIssue addIssue);

}
