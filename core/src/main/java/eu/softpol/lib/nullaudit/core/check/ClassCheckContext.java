package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.ClassLocation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAComponent;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAField;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAMethod;
import eu.softpol.lib.nullaudit.core.report.Kind;

public abstract class ClassCheckContext {

  private final ClassLocation location;
  private final NAClass naClass;

  public ClassCheckContext(ClassLocation location, NAClass naClass) {
    this.location = location;
    this.naClass = naClass;
  }

  public ClassLocation location() {
    return location;
  }

  public NAClass naClass() {
    return naClass;
  }

  public abstract void addIssueForClass(Kind kind, String message);

  public abstract void addIssueForField(NAField field, Kind kind, String message);

  public abstract void addIssueForComponent(NAComponent field, Kind kind, String message);

  public abstract void addIssueForMethod(NAMethod method, Kind kind, String message);

}
