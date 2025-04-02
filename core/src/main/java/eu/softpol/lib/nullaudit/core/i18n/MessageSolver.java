package eu.softpol.lib.nullaudit.core.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageSolver {

  private final ResourceBundle resourceBundle;

  public MessageSolver() {
    this.resourceBundle = ResourceBundle.getBundle("eu.softpol.lib.nullaudit.core.messages");
  }

  public String issueUnspecifiedNullnessClass(String signature, String errorPosition) {
    return MessageFormat.format(
        resourceBundle.getString("issue.UNSPECIFIED_NULLNESS.class"),
        signature,
        errorPosition
    );
  }

  public String issueUnspecifiedNullnessField(String signature, String errorPosition) {
    return MessageFormat.format(
        resourceBundle.getString("issue.UNSPECIFIED_NULLNESS.field"),
        signature,
        errorPosition
    );
  }

  public String issueUnspecifiedNullnessComponent(String signature, String errorPosition) {
    return MessageFormat.format(
        resourceBundle.getString("issue.UNSPECIFIED_NULLNESS.component"),
        signature,
        errorPosition
    );
  }

  public String issueUnspecifiedNullnessMethod(String signature, String errorPosition) {
    return MessageFormat.format(
        resourceBundle.getString("issue.UNSPECIFIED_NULLNESS.method"),
        signature,
        errorPosition
    );
  }

  public String issueIrrelevantAnnotationNullUnMarkedPackage() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION.NullUnMarked.package");
  }

  public String issueIrrelevantAnnotationNullUnMarkedClass() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION.NullUnMarked.class");
  }

  public String issueIrrelevantAnnotationNullUnMarkedMethod() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION.NullUnMarked.method");
  }

  public String issueIrrelevantAnnotationOnPrimitiveField() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION_ON_PRIMITIVE.field");
  }

  public String issueIrrelevantAnnotationOnPrimitiveComponent() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION_ON_PRIMITIVE.component");
  }

  public String issueIrrelevantAnnotationOnPrimitiveMethod() {
    return resourceBundle.getString("issue.IRRELEVANT_ANNOTATION_ON_PRIMITIVE.method");
  }

}
