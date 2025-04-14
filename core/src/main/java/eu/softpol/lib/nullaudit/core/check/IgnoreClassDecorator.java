package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public class IgnoreClassDecorator implements Check {

  private final Check delegate;
  private final Exclusions exclusions;

  public IgnoreClassDecorator(Check delegate, Exclusions exclusions) {
    this.delegate = delegate;
    this.exclusions = exclusions;
  }


  @Override
  public void checkModule() {
    delegate.checkModule();
  }

  @Override
  public void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue) {
    delegate.checkPackage(naPackage, addIssue);
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    if (exclusions.isIgnored(naClass.thisClazz())) {
      return;
    }
    delegate.checkClass(naClass, addIssue);
  }
}
