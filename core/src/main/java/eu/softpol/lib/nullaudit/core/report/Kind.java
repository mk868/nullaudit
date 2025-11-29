package eu.softpol.lib.nullaudit.core.report;

public enum Kind {
  // requireSpecifiedNullness
  UNSPECIFIED_NULLNESS(Category.REQUIRE_SPECIFIED_NULLNESS),
  // requireNullMarked
  MISSING_NULL_MARKED_ANNOTATION(Category.REQUIRE_NULL_MARKED),
  // verifyJSpecifyAnnotations
  INVALID_NULL_MARK_COMBINATION(Category.VERIFY_JSPECIFY_ANNOTATIONS),
  INVALID_NULLNESS_ON_PRIMITIVE(Category.VERIFY_JSPECIFY_ANNOTATIONS),
  NULLABLE_ON_CLASS(Category.VERIFY_JSPECIFY_ANNOTATIONS),
  NON_NULL_ON_CLASS(Category.VERIFY_JSPECIFY_ANNOTATIONS),
  // prohibitNonJSpecifyAnnotations
  PROHIBITED_ANNOTATION(Category.PROHIBIT_NON_JSPECIFY_ANNOTATIONS);

  private final Category category;

  Kind(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return category;
  }
}
