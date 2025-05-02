package eu.softpol.lib.nullaudit.core.report;

public enum Kind {
  UNSPECIFIED_NULLNESS(Category.REQUIRE_SPECIFIED_NULLNESS),
  MISSING_NULL_MARKED_ANNOTATION(Category.REQUIRE_NULL_MARKED),
  INVALID_NULL_MARK_COMBINATION(Category.VERIFY_JSPECIFY_ANNOTATIONS),
  INVALID_NULLNESS_ON_PRIMITIVE(Category.VERIFY_JSPECIFY_ANNOTATIONS);

  private final Category category;

  Kind(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return category;
  }
}
