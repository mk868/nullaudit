package eu.softpol.lib.nullaudit.core.signature;

import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.signature.SignatureReader;

public class FieldSignatureAnalyzer {

  private FieldSignatureAnalyzer() {
  }

  public static TypeNode analyze(String signature) {
    var signatureReader = new SignatureReader(signature);
    var resultRef = new AtomicReference<@Nullable TypeNode>();
    var vis = new FieldSignatureVisitor(resultRef::set);
    signatureReader.acceptType(vis);

    var result = resultRef.get();
    if (result == null) {
      throw new IllegalStateException("Failed to parse field signature: " + signature);
    }
    return result;
  }
}
