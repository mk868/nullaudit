package eu.softpol.lib.nullaudit.core.check.require_specified_nullness;

import static eu.softpol.lib.nullaudit.core.check.CheckUtils.isDefaultRecordAccessorMethod;
import static eu.softpol.lib.nullaudit.core.check.CheckUtils.isDefaultRecordConstructor;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAComponent;
import eu.softpol.lib.nullaudit.core.model.NAField;
import eu.softpol.lib.nullaudit.core.model.NAMethod;
import eu.softpol.lib.nullaudit.core.model.NAMethodParam;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.type.translator.AugmentedStringTranslator;
import eu.softpol.lib.nullaudit.core.util.NullScopeUtil;
import java.util.stream.Collectors;

public class UnspecifiedNullnessCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public UnspecifiedNullnessCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();
    var classEffectiveNullScope = context.effectiveClassNullScope();

    if (classEffectiveNullScope != NullScope.NULL_MARKED) {

      for (var component : naClass.components()) {
        var s = toString(component, classEffectiveNullScope);
        if (s.contains("*")) {
          context.addIssueForComponent(
              component,
              Kind.UNSPECIFIED_NULLNESS,
              messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_COMPONENT,
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }

      if (!naClass.isRecord()) {
        // TODO should we skip static fields?
        for (var field : naClass.fields()) {
          var s = toString(field, classEffectiveNullScope);
          if (s.contains("*")) {
            context.addIssueForField(
                field,
                Kind.UNSPECIFIED_NULLNESS,
                messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_FIELD,
                    s,
                    s.replaceAll("[^*]", " ").replace("*", "^")
                ));
          }
        }
      }
    }

    for (var method : naClass.methods()) {
      var methodEffectiveNullScope = NullScopeUtil.effectiveNullScopeForMethod(
          classEffectiveNullScope, method);

      if (naClass.isRecord()) {
        var methodName = method.methodName();
        if (methodName.equals("equals")) {
          continue;
        }
        if (methodName.equals("toString")) {
          continue;
        }

        if (isDefaultRecordAccessorMethod(naClass, classEffectiveNullScope, method,
            methodEffectiveNullScope)) {
          // skip default accessor
          continue;
        }
        if (isDefaultRecordConstructor(naClass, classEffectiveNullScope, method,
            methodEffectiveNullScope)) {
          // skip default constructor
          continue;
        }
      }

      if (methodEffectiveNullScope != NullScope.NULL_MARKED) {
        var s = toString(method, methodEffectiveNullScope);
        if (s.contains("*")) {
          context.addIssueForMethod(
              method,
              Kind.UNSPECIFIED_NULLNESS,
              messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_METHOD,
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }
  }

  private static String toString(NAComponent component, NullScope nullScope) {
    var classTypeTranslator = AugmentedStringTranslator.of(nullScope);

    return "%s %s".formatted(
        classTypeTranslator.translate(component.type()),
        component.componentName()
    );
  }

  private static String toString(NAField field, NullScope nullScope) {
    var classTypeTranslator = AugmentedStringTranslator.of(nullScope);

    return "%s %s".formatted(
        classTypeTranslator.translate(field.type()),
        field.fieldName()
    );
  }

  private static String toString(NAMethod method, NullScope nullScope) {
    var methodTypeTranslator = AugmentedStringTranslator.of(nullScope);

    return "%s %s(%s)".formatted(
        methodTypeTranslator.translate(method.returnType()),
        method.methodName(),
        method.parameters().stream()
            .map(NAMethodParam::type)
            .map(methodTypeTranslator::translate)
            .collect(Collectors.joining(", "))
    );
  }
}
