package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.IgnoredClasses;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.function.BiConsumer;

public class IgnoreClassDecorator implements Check {

  private final Check delegate;
  private final IgnoredClasses ignoredClasses;

  public IgnoreClassDecorator(Check delegate, IgnoredClasses ignoredClasses) {
    this.delegate = delegate;
    this.ignoredClasses = ignoredClasses;
  }


  @Override
  public void checkModule() {
    delegate.checkModule();
  }

  @Override
  public void checkPackage(VisitedPackage visitedPackage, BiConsumer<Kind, String> addIssue) {
    delegate.checkPackage(visitedPackage, addIssue);
  }

  @Override
  public void checkClass(VisitedClass visitedClass, AddIssue addIssue) {
    if (ignoredClasses.isIgnored(visitedClass.thisClazz())) {
      return;
    }
    delegate.checkClass(visitedClass, addIssue);
  }
}
