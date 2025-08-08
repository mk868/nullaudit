package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.Exclusions;
import eu.softpol.lib.nullaudit.core.matcher.AntLikeFQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.FQCNMatcher;
import eu.softpol.lib.nullaudit.core.matcher.StaticFQCNMatcher;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import java.util.List;

public class IgnoreClassDecorator implements ClassChecker, PackageInfoChecker {

  private final Checker delegate;
  private final List<FQCNMatcher> matchers;
  private final List<String> ignoredAnnotations;

  public IgnoreClassDecorator(Checker delegate, Exclusions exclusions) {
    this.delegate = delegate;
    this.matchers = exclusions.classes().stream()
        .map(
            rule -> rule.contains("*") ? new AntLikeFQCNMatcher(rule) : new StaticFQCNMatcher(rule))
        .toList();
    this.ignoredAnnotations = List.copyOf(exclusions.annotations());
  }

  @Override
  public void checkPackage(PackageInfoCheckContext context) {
    if (!(delegate instanceof PackageInfoChecker packageInfoChecker)) {
      return;
    }
    var packageInfoFQCN = context.naPackage().packageName();
    if (!packageInfoFQCN.isEmpty()) {
      packageInfoFQCN += ".";
    }
    packageInfoFQCN += "package-info";
    for (FQCNMatcher matcher : matchers) {
      if (matcher.matches(packageInfoFQCN)) {
        // package-info ignored
        return;
      }
    }
    packageInfoChecker.checkPackage(context);
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    if (!(delegate instanceof ClassChecker classChecker)) {
      return;
    }
    var naClass = context.naClass();
    for (FQCNMatcher matcher : matchers) {
      if (matcher.matches(naClass.topClass().name())) {
        // class ignored
        return;
      }
    }
    for (var annotation : ignoredAnnotations) {
      if (naClass.annotations().stream().map(NAAnnotation::fqcn)
          .anyMatch(a -> a.equals(annotation))) {
        // class ignored
        return;
      }
    }
    classChecker.checkClass(context);
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
