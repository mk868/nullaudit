package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.NAPackage;
import eu.softpol.lib.nullaudit.core.matcher.AntLikeFQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.FQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.StaticFQCNMatcher;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import java.util.function.BiConsumer;

public class IgnoreClassDecorator implements ClassChecker, PackageInfoChecker {

  private final Checker delegate;
  private final List<FQCNMatcher> matchers;

  public IgnoreClassDecorator(Checker delegate, Exclusions exclusions) {
    this.delegate = delegate;
    this.matchers = exclusions.classes().stream()
        .map(
            rule -> rule.contains("*") ? new AntLikeFQCNMatcher(rule) : new StaticFQCNMatcher(rule))
        .toList();
  }

  @Override
  public void checkPackage(NAPackage naPackage, BiConsumer<Kind, String> addIssue) {
    if (delegate instanceof PackageInfoChecker packageInfoChecker) {
      packageInfoChecker.checkPackage(naPackage, addIssue);
    }
  }

  @Override
  public void checkClass(NAClass naClass, AddIssue addIssue) {
    for (FQCNMatcher matcher : matchers) {
      if (matcher.matches(naClass.topClass().name())) {
        // class ignored
        return;
      }
    }
    if (delegate instanceof ClassChecker classChecker) {
      classChecker.checkClass(naClass, addIssue);
    }
  }
}
