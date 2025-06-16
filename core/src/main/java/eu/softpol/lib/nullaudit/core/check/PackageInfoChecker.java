package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public interface PackageInfoChecker extends Checker {

  void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue);

}
