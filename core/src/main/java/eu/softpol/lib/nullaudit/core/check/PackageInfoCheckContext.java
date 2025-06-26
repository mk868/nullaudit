package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.PackageLocation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class PackageInfoCheckContext {

  private final PackageLocation location;
  private final NAPackage naPackage;
  private final CodeAnalysisData codeAnalysisData;

  public PackageInfoCheckContext(PackageLocation location, NAPackage naPackage,
      CodeAnalysisData codeAnalysisData) {
    this.location = location;
    this.naPackage = naPackage;
    this.codeAnalysisData = codeAnalysisData;
  }

  public PackageLocation location() {
    return location;
  }

  public NAPackage naPackage() {
    return naPackage;
  }

  public void addIssue(Kind kind, String message) {
    addIssue(location, kind, message);
  }

  public void addIssue(CodeLocation location, Kind kind, String message) {
    codeAnalysisData.addIssue(location, kind, message);
  }
}
