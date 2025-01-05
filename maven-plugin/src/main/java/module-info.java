import org.jspecify.annotations.NullMarked;

@NullMarked
module eu.softpol.lib.nullaudit.maven {
  requires eu.softpol.lib.nullaudit.core;
  requires maven.plugin.api;
  requires maven.plugin.annotations;
  requires org.jspecify;
  requires com.google.gson;
  requires java.xml;
}
