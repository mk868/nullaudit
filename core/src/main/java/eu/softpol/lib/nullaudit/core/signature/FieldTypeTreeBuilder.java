package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

public class FieldTypeTreeBuilder extends SignatureVisitor {

  private @Nullable TypeNode node;
  private @Nullable TypeNode root;

  public FieldTypeTreeBuilder() {
    super(Opcodes.ASM9);
  }

  @Override
  public void visitFormalTypeParameter(String name) {
    var clazz = name.replace("/", ".");
    if (root == null) {
      root = node = new ClassTypeNode(clazz);
    } else {
      node = node.addClassChild(clazz);
    }
    super.visitFormalTypeParameter(name);
  }

  @Override
  public void visitClassType(String name) {
    var clazz = name.replace("/", ".");
    if (root == null) {
      root = node = new ClassTypeNode(clazz);
    } else {
      node = node.addClassChild(clazz);
    }
    super.visitClassType(name);
  }

  @Override
  public SignatureVisitor visitArrayType() {
    if (root == null) {
      root = node = new ArrayTypeNode();
    } else {
      node = node.addArrayChild();
    }
    return super.visitArrayType();
  }

  @Override
  public void visitBaseType(char descriptor) {
    if (root == null) {
      root = node = new PrimitiveTypeNode(descriptor);
    } else {
      node = node.addPrimitiveChild(descriptor);
      goBack();
    }
  }

  @Override
  public void visitTypeVariable(String name) {
    if (root == null) {
      root = node = new VariableTypeNode(name);
    } else {
      node = node.addVariableChild(name);
      goBack();
    }
    super.visitTypeVariable(name);
  }

  @Override
  public void visitTypeArgument() {
    node = node.addUnboundedChild();
    goBack();
    super.visitTypeArgument();
  }

  @Override
  public void visitEnd() {
    goBack();
    super.visitEnd();
  }

  private void goBack() {
    node = node.getParent();
    while (node instanceof ArrayTypeNode) {
      node = node.getParent();
    }
  }

  public TypeNode getFieldType() {
    return root;
  }
}
