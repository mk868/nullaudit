package eu.softpol.lib.nullaudit.coretest.matcher;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.matcher.StaticFQCNMatcher;
import org.junit.jupiter.api.Test;

class StaticMatcherTest {

  @Test
  void test() {
    var matcher1 = new StaticFQCNMatcher("com.example.MyClass");
    var matcher2 = new StaticFQCNMatcher("com.example.sub.MyClass");

    assertThat(matcher1.matches("com.example.MyClass")).isTrue();
    assertThat(matcher1.matches("com.example.sub.MyClass")).isFalse();

    assertThat(matcher2.matches("com.example.MyClass")).isFalse();
    assertThat(matcher2.matches("com.example.sub.MyClass")).isTrue();
  }
}
