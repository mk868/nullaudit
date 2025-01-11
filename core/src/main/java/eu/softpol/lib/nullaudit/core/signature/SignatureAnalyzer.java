package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.objectweb.asm.signature.SignatureReader;

public class SignatureAnalyzer {

  public static MethodSignature analyzeMethodSignature(String signature) {
    var signatureReader = new SignatureReader(signature);
    var vis = new MethodTypeTreeBuilder();
    signatureReader.accept(vis);
    var returnType = vis.getReturnType();
    var params = vis.getParams();
    return new MethodSignature(returnType, params);
  }

  public static TypeNode analyzeFieldSignature(String signature) {
    var signatureReader = new SignatureReader(signature);
    var vis = new FieldTypeTreeBuilder();
    signatureReader.accept(vis);
    return vis.getFieldType();
  }
}
