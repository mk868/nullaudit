package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.matcher.AntLikeFQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.FQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.StaticFQCNMatcher;
import java.util.List;

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
  public void checkPackage(PackageInfoCheckContext context) {
    if (delegate instanceof PackageInfoChecker packageInfoChecker) {
      packageInfoChecker.checkPackage(context);
    }
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();
    for (FQCNMatcher matcher : matchers) {
      if (matcher.matches(naClass.topClass().name())) {
        // class ignored
        return;
      }
    }
    if (delegate instanceof ClassChecker classChecker) {
      classChecker.checkClass(context);
    }
  }

  public static Checker of(Checker delegate, Exclusions exclusions) {
    return new IgnoreClassDecorator(delegate, exclusions);
  }

  public static List<Checker> of(List<Checker> delegate, Exclusions exclusions) {
    return delegate.stream()
        .map(x -> IgnoreClassDecorator.of(x, exclusions))
        .toList();
  }
}
