open module eu.softpol.lib.nullaudit.coretest {
  requires transitive eu.softpol.lib.nullaudit.core;
  requires transitive io.github.ascopes.jct;
  requires java.compiler;
  requires transitive org.assertj.core;
  requires org.junit.jupiter.params;
  requires org.jspecify;
  requires org.junit.jupiter.api;
  requires net.bytebuddy;
}
