package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAComponent;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAField;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAMethod;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public interface Check {

  interface AddIssue {

    void addIssueForClass(Kind kind, String message);

    void addIssueForField(NAField field, Kind kind, String message);

    void addIssueForComponent(NAComponent field, Kind kind, String message);

    void addIssueForMethod(NAMethod method, Kind kind, String message);
  }

  void checkModule();

  void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue);

  void checkClass(NAClass naClass, AddIssue addIssue);

}
