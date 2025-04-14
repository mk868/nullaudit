package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public interface Check {

  interface AddIssue {

    void addIssueForClass(Kind kind, String message);

    void addIssueForField(String name, Kind kind, String message);

    void addIssueForMethod(String name, Kind kind, String message);
  }

  void checkModule();

  void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue);

  void checkClass(NAClass naClass, AddIssue addIssue);

}
