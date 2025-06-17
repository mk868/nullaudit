package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData;
import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData.IssueEntry;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.ClassLocation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAComponent;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAField;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAMethod;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import org.jspecify.annotations.Nullable;

public class ClassCheckContext {

  private final ClassLocation location;
  private final @Nullable NAPackage naPackage;
  private final NAClass naClass;
  private final CodeAnalysisData codeAnalysisData;

  public ClassCheckContext(ClassLocation location, @Nullable NAPackage naPackage, NAClass naClass,
      CodeAnalysisData codeAnalysisData) {
    this.location = location;
    this.naPackage = naPackage;
    this.naClass = naClass;
    this.codeAnalysisData = codeAnalysisData;
  }

  public ClassLocation location() {
    return location;
  }

  public @Nullable NAPackage naPackage() {
    return naPackage;
  }

  public NAClass naClass() {
    return naClass;
  }

  public void addIssueForClass(Kind kind, String message) {
    addIssue(location, kind, message);
  }

  public void addIssueForField(NAField field, Kind kind, String message) {
    addIssue(location.memberLocation(field.fieldName()), kind, message);
  }

  public void addIssueForComponent(NAComponent component, Kind kind, String message) {
    addIssue(location.memberLocation(component.componentName()), kind, message);
  }

  public void addIssueForMethod(NAMethod method, Kind kind, String message) {
    addIssue(location.memberLocation(method.descriptiveMethodName()), kind, message);
  }

  public void addIssue(CodeLocation codeLocation, Kind kind, String message) {
    codeAnalysisData.addIssue(codeLocation, kind, message);
  }

  public List<IssueEntry> getIssues(CodeLocation codeLocation) {
    return List.copyOf(codeAnalysisData.getIssues(codeLocation));
  }
}
