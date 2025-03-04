open module eu.softpol.lib.nullaudit.coretest {
  requires transitive eu.softpol.lib.nullaudit.core;
  requires transitive io.github.ascopes.jct;
  requires java.compiler;
  requires truth;
  requires org.junit.jupiter.params;
  requires junit;
  requires com.google.common;
  requires org.jspecify;
  requires org.junit.jupiter.api;
}
