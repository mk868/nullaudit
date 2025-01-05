package eu.softpol.lib.nullaudit.core.comparator;

import java.util.Comparator;

public class ClassNameComparator implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    var prefix = getCommonPrefix(o1, o2);
    o1 = o1.substring(prefix.length());
    o2 = o2.substring(prefix.length());
    if (o1.startsWith(".") && o2.startsWith("$")) {
      return -1;
    }
    if (o1.startsWith("$") && o2.startsWith(".")) {
      return 1;
    }
    return o1.compareTo(o2);
  }

  private static String getCommonPrefix(String a, String b) {
    int minLength = Math.min(a.length(), b.length());
    for (int i = 0; i < minLength; i++) {
      if (a.charAt(i) != b.charAt(i)) {
        return a.substring(0, i);
      }
    }
    return a.substring(0, minLength);
  }
}
