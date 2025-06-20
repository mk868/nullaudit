package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.CodeLocation.PackageLocation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;

public abstract class PackageInfoCheckContext {

  private final PackageLocation location;
  private final NAPackage naPackage;

  public PackageInfoCheckContext(PackageLocation location, NAPackage naPackage) {
    this.location = location;
    this.naPackage = naPackage;
  }

  public PackageLocation location() {
    return location;
  }

  public NAPackage naPackage() {
    return naPackage;
  }

  public abstract void addIssue(Kind kind, String message);
}
