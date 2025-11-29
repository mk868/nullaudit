package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.model.NAClass;
import eu.softpol.lib.nullaudit.core.model.NAComponent;
import eu.softpol.lib.nullaudit.core.model.NAMethod;
import eu.softpol.lib.nullaudit.core.model.NAMethodParam;
import eu.softpol.lib.nullaudit.core.type.translator.AugmentedStringTranslator;
import java.util.stream.Collectors;

public class CheckUtils {

  public static boolean isDefaultRecordConstructor(NAClass naClass, NullScope classNullScope,
      NAMethod method, NullScope methodNullScope) {
    var methodName = method.methodName();

    if (!methodName.equals("<init>")) {
      return false;
    }

    var classTypeTranslator = AugmentedStringTranslator.of(classNullScope);
    var methodTypeTranslator = AugmentedStringTranslator.of(methodNullScope);

    var expectedSignature = naClass.components().stream()
        .map(NAComponent::type)
        .map(classTypeTranslator::translate)
        .collect(Collectors.joining(","));
    var paramSignature = method.parameters().stream()
        .map(NAMethodParam::type)
        .map(methodTypeTranslator::translate)
        .collect(Collectors.joining(","));

    return paramSignature.equals(expectedSignature);
  }

  public static boolean isDefaultRecordAccessorMethod(NAClass naClass, NullScope classNullScope,
      NAMethod method, NullScope methodNullScope) {
    var methodName = method.methodName();

    if (!method.parameters().isEmpty()) {
      return false;
    }

    var classTypeTranslator = AugmentedStringTranslator.of(classNullScope);
    var methodTypeTranslator = AugmentedStringTranslator.of(methodNullScope);

    var returnTypeStr = methodTypeTranslator.translate(method.returnType());
    return naClass.getComponent(methodName)
        .map(c -> classTypeTranslator.translate(c.type()))
        .filter(returnTypeStr::equals)
        .isPresent();
  }
}
