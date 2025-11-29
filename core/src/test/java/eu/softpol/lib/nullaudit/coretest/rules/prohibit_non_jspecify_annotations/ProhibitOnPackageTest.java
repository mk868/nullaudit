package eu.softpol.lib.nullaudit.coretest.rules.prohibit_non_jspecify_annotations;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ProhibitOnPackageTest {

  @Test
  void shouldDetectAnnotationOnPackage() {
    throw new RuntimeException();
  }

}
