package eu.softpol.lib.nullaudit.core.signature;

import org.objectweb.asm.signature.SignatureReader;

public class SignatureAnalyzer {

  public static MethodSignature analyze(String signature) {
    var signatureReader = new SignatureReader(signature);
    var vis = new TypeTreeBuilder();
    signatureReader.accept(vis);
    var returnType = vis.getReturnType();
    var params = vis.getParams();
    return new MethodSignature(returnType, params);
  }
}
