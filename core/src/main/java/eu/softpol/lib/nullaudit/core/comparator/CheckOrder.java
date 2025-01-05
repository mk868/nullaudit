package eu.softpol.lib.nullaudit.core.comparator;

import static eu.softpol.lib.nullaudit.core.comparator.ValueFirst.valueFirst;

import java.util.Comparator;

public class CheckOrder {

  public static final Comparator<String> COMPARATOR =
      valueFirst("module-info.class",
          valueFirst("module-info",
              valueFirst("package-info.class",
                  valueFirst("package-info",
                      new ClassNameComparator()))));

  private CheckOrder() {
  }
}
