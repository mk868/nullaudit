package eu.softpol.lib.nullaudit.core.check.require_specified_nullness;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAComponent;
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

    var classAugmentedStringTranslator = AugmentedStringTranslator.of(classEffectiveNullScope);

    if (classEffectiveNullScope != NullScope.NULL_MARKED) {

      for (var componentInfo : naClass.components()) {
        var s = "%s %s".formatted(
            classAugmentedStringTranslator.translate(componentInfo.type()),
            componentInfo.componentName()
        );
        if (s.contains("*")) {
          context.addIssueForComponent(
              componentInfo,
              Kind.UNSPECIFIED_NULLNESS,
              messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_COMPONENT,
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }

      if (!naClass.isRecord()) {
        for (var fieldInfo : naClass.fields()) {
          var s = "%s %s".formatted(
              classAugmentedStringTranslator.translate(fieldInfo.type()),
              fieldInfo.fieldName()
          );
          if (s.contains("*")) {
            context.addIssueForField(
                fieldInfo,
                Kind.UNSPECIFIED_NULLNESS,
                messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_FIELD,
                    s,
                    s.replaceAll("[^*]", " ").replace("*", "^")
                ));
          }
        }
      }
    }

    for (var methodInfo : naClass.methods()) {

      if (naClass.isRecord()) {
        var methodName = methodInfo.methodName();
        if (methodName.equals("equals")) {
          continue;
        }
        if (methodName.equals("toString")) {
          continue;
        }

        if (naClass.getComponent(methodName)
                .filter(c -> classAugmentedStringTranslator.translate(c.type())
                    .equals(classAugmentedStringTranslator.translate(methodInfo.returnType())))
                .isPresent()
            && methodInfo.parameters().isEmpty()
        ) {
          // skip default getter
          continue;
        }
        if (methodName.equals("<init>") && methodInfo.parameters().stream()
            .map(NAMethodParam::type)
            .map(classAugmentedStringTranslator::translate)
            .collect(Collectors.joining(",")).equals(naClass.components().stream()
                .map(NAComponent::type)
                .map(classAugmentedStringTranslator::translate)
                .collect(Collectors.joining(",")))
        ) {
          // skip default constructor
          continue;
        }
      }

      var methodEffectiveNullScope = NullScopeUtil.effectiveNullScopeForMethod(
          classEffectiveNullScope, methodInfo);

      if (methodEffectiveNullScope != NullScope.NULL_MARKED) {
        var augmentedStringTranslator = AugmentedStringTranslator.of(methodEffectiveNullScope);
        var s = "%s %s(%s)".formatted(
            augmentedStringTranslator.translate(methodInfo.returnType()),
            methodInfo.methodName(),
            methodInfo.parameters().stream()
                .map(NAMethodParam::type)
                .map(augmentedStringTranslator::translate)
                .collect(Collectors.joining(", "))
        );
        if (s.contains("*")) {
          context.addIssueForMethod(
              methodInfo,
              Kind.UNSPECIFIED_NULLNESS,
              messageSolver.resolve(MessageKey.ISSUE_UNSPECIFIED_NULLNESS_METHOD,
                  s,
                  s.replaceAll("[^*]", " ").replace("*", "^")
              ));
        }
      }
    }
  }
}
