package eu.softpol.lib.nullaudit.core.type.translator;

import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.UnboundedTypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import java.util.stream.Collectors;

public class RawStringTranslator implements Translator<String> {

  public static final RawStringTranslator INSTANCE = new RawStringTranslator();

  private RawStringTranslator() {
  }

  @Override
  public String translate(TypeNode typeNode) {
    if (typeNode instanceof ArrayTypeNode arrayTypeNode) {
      return translate(arrayTypeNode.getChildren().get(0)) + "[]";
    } else if (typeNode instanceof PrimitiveTypeNode primitiveTypeNode) {
      return primitiveTypeNode.getName();
    } else if (typeNode instanceof ClassTypeNode classTypeNode) {
      var result = classTypeNode.getClazz();
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
    }
    throw new UnsupportedOperationException();
  }
}
