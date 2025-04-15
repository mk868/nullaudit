package eu.softpol.lib.nullaudit.coretest.matcher;

import static org.assertj.core.api.Assertions.assertThat;

import eu.softpol.lib.nullaudit.core.matcher.AntLikeFQCNMatcher;
import org.junit.jupiter.api.Test;

class AntLikeFQCNMatcherTest {

  @Test
  void test() {
    var matcher1 = new AntLikeFQCNMatcher("com.example.*");
    var matcher2 = new AntLikeFQCNMatcher("com.example.**");

    assertThat(matcher1.matches("com.example.MyClass")).isTrue();
    assertThat(matcher1.matches("com.example.sub.MyClass")).isFalse();

    assertThat(matcher2.matches("com.example.MyClass")).isTrue();
    assertThat(matcher2.matches("com.example.sub.MyClass")).isTrue();
    assertThat(matcher2.matches("com.example.sub.sub2.MyClass")).isTrue();
    assertThat(matcher2.matches("com.example2.MyClass")).isFalse();
  }
}
