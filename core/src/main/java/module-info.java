import org.jspecify.annotations.NullMarked;

@NullMarked
module eu.softpol.lib.nullaudit.core {
  exports eu.softpol.lib.nullaudit.core;
  exports eu.softpol.lib.nullaudit.core.report;
  exports eu.softpol.lib.nullaudit.core.comparator;
  exports eu.softpol.lib.nullaudit.core.analyzer;

  requires org.objectweb.asm;
  requires org.jspecify;
  requires java.naming;
}