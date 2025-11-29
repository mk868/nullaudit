package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

public class MethodSignatureVisitor extends SignatureVisitor {

  private final List<TypeNode> params = new ArrayList<>();
  private @Nullable TypeNode returnType;

  public MethodSignatureVisitor() {
    super(Opcodes.ASM9);
  }

  @Override
  public SignatureVisitor visitParameterType() {
    return new FieldSignatureVisitor(params::add);
  }

  @Override
  public SignatureVisitor visitReturnType() {
    return new FieldSignatureVisitor(node -> this.returnType = node);
  }

  public List<TypeNode> getParams() {
    return List.copyOf(params);
  }

  public TypeNode getReturnType() {
    if (returnType == null) {
      throw new IllegalStateException("Return type not yet built");
    }
    return returnType;
  }
}
