package eu.softpol.lib.nullaudit.core.check;

public interface PackageInfoChecker extends Checker {

  void checkPackage(PackageInfoCheckContext context);

}
