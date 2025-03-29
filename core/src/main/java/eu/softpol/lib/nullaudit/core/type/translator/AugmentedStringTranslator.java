package eu.softpol.lib.nullaudit.core.type.translator;

import eu.softpol.lib.nullaudit.core.analyzer.NullnessOperator;
import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.UnboundedTypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import java.util.stream.Collectors;

public class AugmentedStringTranslator implements Translator<String> {

  public static final AugmentedStringTranslator INSTANCE = new AugmentedStringTranslator();

  private AugmentedStringTranslator() {
  }

  @Override
  public String translate(TypeNode typeNode) {
    if (typeNode instanceof ArrayTypeNode arrayTypeNode) {
      return translate(arrayTypeNode.getChildren().get(0)) + "[]"
             + nullnessOperatorToString(arrayTypeNode.getOperator());
    } else if (typeNode instanceof PrimitiveTypeNode primitiveTypeNode) {
      return primitiveTypeNode.getName();
    } else if (typeNode instanceof ClassTypeNode classTypeNode) {
      var result = classTypeNode.getClazz() + nullnessOperatorToString(classTypeNode.getOperator());
      if (!classTypeNode.getChildren().isEmpty()) {
        result += "<" + classTypeNode.getChildren().stream()
            .map(this::translate)
            .collect(Collectors.joining(", "))
                  + ">";
      }
      return result;
    } else if (typeNode instanceof UnboundedTypeNode) {
      return "?";
    } else if (typeNode instanceof VariableTypeNode variableTypeNode) {
      return variableTypeNode.getName();
    }
    throw new UnsupportedOperationException();
  }

  private String nullnessOperatorToString(NullnessOperator nullnessOperator) {
    return switch (nullnessOperator) {
      case UNION_NULL -> "?";
      case MINUS_NULL -> "!";
      default -> "*";
    };
  }
}
