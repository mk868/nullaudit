package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.UnboundedTypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import eu.softpol.lib.nullaudit.core.type.WildcardTypeNode;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

public class FieldSignatureVisitor extends SignatureVisitor {

  private final Consumer<TypeNode> onResult;

  private ClassTypeNode.@Nullable Builder currentClassBuilder;

  public FieldSignatureVisitor(Consumer<TypeNode> onResult) {
    super(Opcodes.ASM9);
    this.onResult = onResult;
  }

  @Override
  public void visitClassType(String name) {
    currentClassBuilder = ClassTypeNode.builder(name.replace("/", "."));
  }

  @Override
  public SignatureVisitor visitArrayType() {
    return new FieldSignatureVisitor(childNode -> {
      var arrayNode = ArrayTypeNode.builder()
          .withComponentType(childNode)
          .build();
      onResult.accept(arrayNode);
    });
  }

  @Override
  public void visitBaseType(char descriptor) {
    onResult.accept(PrimitiveTypeNode.builder().descriptor(descriptor).build());
  }

  @Override
  public void visitTypeVariable(String name) {
    onResult.accept(VariableTypeNode.builder().name(name).build());
  }

  @Override
  public void visitTypeArgument() {
    if (currentClassBuilder != null) {
      currentClassBuilder.addChild(UnboundedTypeNode.builder().build());
    }
  }

  @Override
  public SignatureVisitor visitTypeArgument(char wildcard) {
    return new FieldSignatureVisitor(childNode -> {
      if (currentClassBuilder != null) {
        if (wildcard == '=') {
          currentClassBuilder.addChild(childNode);
        } else if (wildcard == '+') {
          // ? extends T
          var wildcardNode = WildcardTypeNode.builder()
              .bound(WildcardTypeNode.Bound.EXTENDS)
              .boundType(childNode)
              .build();
          currentClassBuilder.addChild(wildcardNode);
        } else if (wildcard == '-') {
          // ? super T
          var wildcardNode = WildcardTypeNode.builder()
              .bound(WildcardTypeNode.Bound.SUPER)
              .boundType(childNode)
              .build();
          currentClassBuilder.addChild(wildcardNode);
        }
      }
    });
  }

  @Override
  public void visitEnd() {
    if (currentClassBuilder != null) {
      onResult.accept(currentClassBuilder.build());
      currentClassBuilder = null;
    }
    super.visitEnd();
  }

}
