package eu.softpol.lib.nullaudit.core.util;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassReference;
import eu.softpol.lib.nullaudit.core.model.NAClass;
import eu.softpol.lib.nullaudit.core.model.NAMethod;
import eu.softpol.lib.nullaudit.core.model.NAModule;
import eu.softpol.lib.nullaudit.core.model.NAPackage;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public class NullScopeUtil {

  public static NullScope effectiveNullScopeForClass(
      @Nullable NAModule naModule,
      @Nullable NAPackage naPackage,
      Map<ClassReference, NAClass> outerClasses,
      NAClass naClass
  ) {
    var scope = NullScope.from(naClass.annotations());
    if (scope != NullScope.NOT_DEFINED) {
      return scope;
    }
    var classToCheck = naClass.outerClass();
    while (classToCheck != null) {
      var outerClass = outerClasses.get(classToCheck);
      scope = NullScope.from(outerClass.annotations());
      if (scope != NullScope.NOT_DEFINED) {
        return scope;
      }
      classToCheck = outerClass.outerClass();
    }
    if (naPackage != null) {
      scope = NullScope.from(naPackage.annotations());
      if (scope != NullScope.NOT_DEFINED) {
        return scope;
      }
    }
    if (naModule != null) {
      scope = NullScope.from(naModule.annotations());
      if (scope != NullScope.NOT_DEFINED) {
        return scope;
      }
    }
    return NullScope.NULL_UNMARKED;
  }

  public static NullScope effectiveNullScopeForMethod(
      NullScope classEffectiveNullScope,
      NAMethod naMethod
  ) {
    var scope = NullScope.from(naMethod.annotations());
    if (scope != NullScope.NOT_DEFINED) {
      return scope;
    }
    return classEffectiveNullScope;
  }
}
