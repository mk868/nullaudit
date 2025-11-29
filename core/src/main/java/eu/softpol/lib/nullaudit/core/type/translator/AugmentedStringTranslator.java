package eu.softpol.lib.nullaudit.core.type.translator;

import eu.softpol.lib.nullaudit.core.analyzer.NullScope;
import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.UnboundedTypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import eu.softpol.lib.nullaudit.core.type.WildcardTypeNode;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AugmentedStringTranslator implements Translator<String> {

  private static final Map<NullScope, AugmentedStringTranslator> CACHE = new EnumMap<>(
      NullScope.class);

  private final NullScope scope;

  private AugmentedStringTranslator(NullScope scope) {
    this.scope = scope;
  }

  @Override
  public String translate(TypeNode typeNode) {
    if (typeNode instanceof ArrayTypeNode arrayTypeNode) {
      return translate(arrayTypeNode.getChildren().get(0)) + "[]"
             + nullnessOperatorToString(arrayTypeNode);
    } else if (typeNode instanceof PrimitiveTypeNode primitiveTypeNode) {
      return primitiveTypeNode.getName();
    } else if (typeNode instanceof ClassTypeNode classTypeNode) {
      var result = classTypeNode.getClazz() + nullnessOperatorToString(classTypeNode);
      if (!classTypeNode.getChildren().isEmpty()) {
        result += classTypeNode.getChildren().stream()
            .map(this::translate)
            .collect(Collectors.joining(", ", "<", ">"));
      }
      return result;
    } else if (typeNode instanceof UnboundedTypeNode) {
      return "?";
    } else if (typeNode instanceof VariableTypeNode variableTypeNode) {
      return variableTypeNode.getName();
    } else if (typeNode instanceof WildcardTypeNode wildcardTypeNode) {
      return "? %s %s".formatted(
          wildcardTypeNode.getBound() == WildcardTypeNode.Bound.EXTENDS ? "extends" : "super",
          translate(wildcardTypeNode.getBoundType())
      );
    }
    throw new UnsupportedOperationException();
  }

  private String nullnessOperatorToString(TypeNode typeNode) {
    var operator = NullnessOperator.UNSPECIFIED;
    var annotations = typeNode.getAnnotations();
    var nullable = annotations.contains(TypeUseAnnotation.JSPECIFY_NULLABLE);
    var notNull = annotations.contains(TypeUseAnnotation.JSPECIFY_NON_NULL);
    if (nullable && !notNull) {
      operator = NullnessOperator.UNION_NULL;
    } else if (!nullable && notNull) {
      operator = NullnessOperator.MINUS_NULL;
    }
    if (scope == NullScope.NULL_MARKED && operator == NullnessOperator.UNSPECIFIED) {
      operator = NullnessOperator.MINUS_NULL;
    }
    return switch (operator) {
      case UNION_NULL -> "?";
      case MINUS_NULL -> "!";
      default -> "*";
    };
  }

  public static AugmentedStringTranslator of(NullScope scope) {
    return CACHE.computeIfAbsent(scope, AugmentedStringTranslator::new);
  }
}
