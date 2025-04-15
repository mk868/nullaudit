package eu.softpol.lib.nullaudit.core.matcher;

import java.util.regex.Pattern;

public class AntLikeFQCNMatcher implements FQCNMatcher {

  private final Pattern pattern;

  public AntLikeFQCNMatcher(String antLikePattern) {
    String regex = antLikePatternToRegex(antLikePattern);
    this.pattern = Pattern.compile(regex);
  }

  private String antLikePatternToRegex(String antPattern) {
    StringBuilder regex = new StringBuilder();
    char[] chars = antPattern.toCharArray();

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '*') {
        if (i + 1 < chars.length && chars[i + 1] == '*') {
          // '**' matches zero or more package segments
          regex.append(".*");
          i++;
        } else {
          // '*' matches zero or more chars except '.'
          regex.append("[^.]*");
        }
      } else if (c == '.') {
        regex.append("\\.");
      } else {
        regex.append(Pattern.quote(String.valueOf(c)));
      }
    }

    // Match entire input
    return "^" + regex + "$";
  }

  public boolean matches(String fqcn) {
    return pattern.matcher(fqcn).matches();
  }
}
