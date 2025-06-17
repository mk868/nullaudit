package eu.softpol.lib.nullaudit.maven.i18n;

public enum MessageKey {
  CHECK_ISSUES_FOUND("plugin.check.issuesFound", 1),
  CHECK_MORE_ISSUES("plugin.check.moreIssues", 1),
  REPORT_REPORT_SAVED("plugin.report.reportSaved", 1),
  SKIPPING_POM_PACKAGING("plugin.common.skippingPomPackaging"),
  SKIPPING_TILE_PACKAGING("plugin.common.skippingTilePackaging");

  private final String key;
  private final int argCount;

  MessageKey(String key) {
    this(key, 0);
  }

  MessageKey(String key, int argCount) {
    this.key = key;
    this.argCount = argCount;
  }

  public String key() {
    return key;
  }

  public int argCount() {
    return argCount;
  }
}
