package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.ArrayTypeNode;
import eu.softpol.lib.nullaudit.core.type.ClassTypeNode;
import eu.softpol.lib.nullaudit.core.type.PrimitiveTypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.VariableTypeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

public class MethodSignatureVisitor extends SignatureVisitor {

  private final Map<TypeNode, TypeNode> nodeToParent = new HashMap<>();
  private final List<TypeNode> params = new ArrayList<>();
  private @Nullable TypeNode returnType;

  int paramIndex = -1;

  enum CHECKING_ELEMENT {
    PARAM, RETURN, EXCEPTION;
  }

  private CHECKING_ELEMENT checkingElement = CHECKING_ELEMENT.PARAM;
  private @Nullable TypeNode node;
  private @Nullable TypeNode root;

  public MethodSignatureVisitor() {
    super(Opcodes.ASM9);
  }

  @Override
  public SignatureVisitor visitParameterType() {
    if (paramIndex >= 0) {
      params.add(root);
    }
    checkingElement = CHECKING_ELEMENT.PARAM;
    root = null;
    paramIndex++;
    return super.visitParameterType();
  }

  @Override
  public SignatureVisitor visitReturnType() {
    if (paramIndex >= 0) {
      params.add(root);
    }
    root = null;
    checkingElement = CHECKING_ELEMENT.RETURN;
    return super.visitReturnType();
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    if (returnType == null && checkingElement == CHECKING_ELEMENT.RETURN) {
      returnType = root;
    }
    // TODO not implemented
    root = null;
    checkingElement = CHECKING_ELEMENT.EXCEPTION;
    return super.visitExceptionType();
  }

  @Override
  public void visitFormalTypeParameter(String name) {
    var clazz = name.replace("/", ".");
    if (root == null) {
      root = node = new ClassTypeNode(clazz);
    } else {
      goToChild(node.addClassChild(clazz));
    }
    super.visitFormalTypeParameter(name);
  }

  @Override
  public void visitClassType(String name) {
    var clazz = name.replace("/", ".");
    if (root == null) {
      root = node = new ClassTypeNode(clazz);
    } else {
      goToChild(node.addClassChild(clazz));
    }
    super.visitClassType(name);
  }

  @Override
  public SignatureVisitor visitArrayType() {
    if (root == null) {
      root = node = new ArrayTypeNode();
    } else {
      goToChild(node.addArrayChild());
    }
    return super.visitArrayType();
  }

  @Override
  public void visitBaseType(char descriptor) {
    if (root == null) {
      root = node = new PrimitiveTypeNode(descriptor);
    } else {
      goToChild(node.addPrimitiveChild(descriptor));
      goBack();
    }
  }

  @Override
  public void visitTypeVariable(String name) {
    if (root == null) {
      root = node = new VariableTypeNode(name);
    } else {
      goToChild(node.addVariableChild(name));
      goBack();
    }
    super.visitTypeVariable(name);
  }

  @Override
  public void visitTypeArgument() {
    goToChild(node.addUnboundedChild());
    goBack();
    super.visitTypeArgument();
  }

  @Override
  public void visitEnd() {
    goBack();
    super.visitEnd();
  }

  private void goBack() {
    node = nodeToParent.get(node);
    while (node instanceof ArrayTypeNode) {
      node = nodeToParent.get(node);
    }
  }

  private void goToChild(TypeNode child) {
    nodeToParent.put(child, node);
    node = child;
  }

  public List<TypeNode> getParams() {
    return List.copyOf(params);
  }

  public TypeNode getReturnType() {
    if (returnType == null && checkingElement == CHECKING_ELEMENT.RETURN) {
      returnType = root;
      root = null;
    }
    if (returnType == null) {
      throw new IllegalStateException("Return type not yet built");
    }
    return returnType;
  }
}
