package eu.softpol.lib.nullaudit.core.matcher;

public class StaticFQCNMatcher implements FQCNMatcher {

  private final String fqcn;

  public StaticFQCNMatcher(String fqcn) {
    this.fqcn = fqcn;
  }

  public boolean matches(String fqcn) {
    return this.fqcn.equals(fqcn);
  }
}
