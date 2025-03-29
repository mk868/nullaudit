package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import org.objectweb.asm.signature.SignatureReader;

public class FieldSignatureAnalyzer {

  private FieldSignatureAnalyzer() {
  }

  public static TypeNode analyze(String signature) {
    var signatureReader = new SignatureReader(signature);
    var vis = new FieldSignatureVisitor();
    signatureReader.acceptType(vis);
    return vis.getFieldType();
  }
}
