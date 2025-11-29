package eu.softpol.lib.nullaudit.core.analyzer.visitor;

import eu.softpol.lib.nullaudit.core.annotation.TypeUseAnnotation;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAMethod;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAMethod.Builder;
import eu.softpol.lib.nullaudit.core.model.ImmutableNAMethodParam;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import eu.softpol.lib.nullaudit.core.type.TypeNodeAnnotator;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;

/**
 * Appends nullness info to the type signature
 */
public class MyMethodVisitor extends MethodVisitor {

  private static final System.Logger logger = System.getLogger(MyMethodVisitor.class.getName());

  private final ImmutableNAMethod.Builder naMethodBuilder;
  private TypeNode returnType;
  private final List<TypeNode> parameterTypes;
  private final List<ImmutableNAMethodParam.Builder> parametersBuilders;
  private final Runnable onEnd;

  protected MyMethodVisitor(Builder naMethodBuilder, TypeNode returnType,
      List<TypeNode> parameterTypes, Runnable onEnd) {
    super(Opcodes.ASM9);
    this.naMethodBuilder = naMethodBuilder;
    this.returnType = returnType;
    this.parameterTypes = new ArrayList<>(parameterTypes);
    this.parametersBuilders = parameterTypes.stream()
        .map(t -> ImmutableNAMethodParam.builder().type(t))
        .toList();
    this.onEnd = onEnd;
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    super.visitLineNumber(line, start);
  }

  @Override
  public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor,
      boolean visible) {
    var parameterBuilder = parametersBuilders.get(parameter);
    parameterBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitParameterAnnotation(parameter, descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath,
      String descriptor, boolean visible) {
    var annotation = TypeUseAnnotation.ofDescriptor(descriptor);
    var typeReference = new TypeReference(typeRef);
    var sort = typeReference.getSort();
    var typePathStr = typePath == null ? "" : typePath.toString();
    if (sort == TypeReference.METHOD_RETURN) {
      handleMethodReturn(typePath, typePathStr, annotation);
    } else if (sort == TypeReference.METHOD_FORMAL_PARAMETER) {
      handleMethodFormalParameter(typePath, typeReference, typePathStr, annotation);
    } else if (sort == TypeReference.METHOD_TYPE_PARAMETER_BOUND) {
      logger.log(Level.DEBUG, "METHOD_TYPE_PARAMETER_BOUND not supported yet");
    } else if (sort == TypeReference.METHOD_TYPE_PARAMETER) {
      logger.log(Level.DEBUG, "METHOD_TYPE_PARAMETER not supported yet");
    } else {
      throw new UnsupportedOperationException("Unsupported sort: " + sort);
    }
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  private void handleMethodReturn(@Nullable TypePath typePath, String typePathStr,
      TypeUseAnnotation annotation) {
    if (typePathStr.contains("*")) {
      // TODO super not yet supported
    } else if (typePathStr.contains(".")) {
      // TODO how to handle this case...
    } else {
      this.returnType = TypeNodeAnnotator.annotate(this.returnType, typePath, annotation);
    }
  }

  private void handleMethodFormalParameter(@Nullable TypePath typePath, TypeReference typeReference,
      String typePathStr,
      TypeUseAnnotation annotation) {
    var index = typeReference.getFormalParameterIndex();
    if (typePathStr.contains("*")) {
      // TODO super not yet supported
    } else if (typePathStr.contains(".")) {
      // TODO how to handle this case...
    } else {
      TypeNode oldType = parameterTypes.get(index);
      TypeNode newType = TypeNodeAnnotator.annotate(oldType, typePath, annotation);
      parameterTypes.set(index, newType);
    }
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    naMethodBuilder.addAnnotations(NAAnnotation.fromDescriptor(descriptor));
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
    var finalParameters = new ArrayList<ImmutableNAMethodParam>();
    for (int i = 0; i < parameterTypes.size(); i++) {
      var type = parameterTypes.get(i);
      var builder = parametersBuilders.get(i);

      builder.type(type);
      finalParameters.add(builder.build());
    }
    naMethodBuilder.returnType(this.returnType);
    naMethodBuilder.parameters(finalParameters);
    onEnd.run();
  }
}
