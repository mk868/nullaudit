package eu.softpol.lib.nullaudit.core.analyzer;

import eu.softpol.lib.nullaudit.core.analyzer.visitor.ClassReference;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public class AnalysisContext {

  private @Nullable String moduleName;
  private boolean moduleInfoNullMarked;
  private final Map<String, NullScope> packageNullScope = new HashMap<>();
  private final Map<ClassReference, NullScope> classNullScope = new HashMap<>();

  public @Nullable String getModuleName() {
    return moduleName;
  }

  public void setModuleName(@Nullable String moduleName) {
    this.moduleName = moduleName;
  }

  public boolean isModuleInfoNullMarked() {
    return moduleInfoNullMarked;
  }

  public void setModuleInfoNullMarked(boolean moduleInfoNullMarked) {
    this.moduleInfoNullMarked = moduleInfoNullMarked;
  }

  public NullScope getPackageNullScope(String packageName) {
    return packageNullScope.getOrDefault(packageName, NullScope.NOT_DEFINED);
  }

  public void setPackageNullScope(String packageName, NullScope nullScope) {
    this.packageNullScope.put(packageName, nullScope);
  }

  public NullScope getClassNullScope(ClassReference classReference) {
    return classNullScope.getOrDefault(classReference, NullScope.NOT_DEFINED);
  }

  public void setClassNullScope(ClassReference classReference, NullScope nullScope) {
    this.classNullScope.put(classReference, nullScope);
  }
}
