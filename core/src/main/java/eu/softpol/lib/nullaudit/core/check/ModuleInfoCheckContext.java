package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.CodeAnalysisData;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation;
import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.ModuleLocation;
import eu.softpol.lib.nullaudit.core.model.NAModule;
import eu.softpol.lib.nullaudit.core.report.Kind;

public class ModuleInfoCheckContext {

  private final ModuleLocation location;
  private final NAModule naModule;
  private final CodeAnalysisData codeAnalysisData;

  public ModuleInfoCheckContext(ModuleLocation location, NAModule naModule,
      CodeAnalysisData codeAnalysisData) {
    this.location = location;
    this.naModule = naModule;
    this.codeAnalysisData = codeAnalysisData;
  }

  public ModuleLocation location() {
    return location;
  }

  public NAModule naModule() {
    return naModule;
  }

  public void addIssue(Kind kind, String message) {
    addIssue(location, kind, message);
  }

  public void addIssue(CodeLocation location, Kind kind, String message) {
    codeAnalysisData.addIssue(location, kind, message);
  }
}
