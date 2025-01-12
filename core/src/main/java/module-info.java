import org.jspecify.annotations.NullMarked;

@NullMarked
module eu.softpol.lib.nullaudit.core {
  exports eu.softpol.lib.nullaudit.core;
  exports eu.softpol.lib.nullaudit.core.report;
  exports eu.softpol.lib.nullaudit.core.comparator;
  exports eu.softpol.lib.nullaudit.core.analyzer;
  exports eu.softpol.lib.nullaudit.core.signature to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.type to eu.softpol.lib.nullaudit.coretest;

  requires org.objectweb.asm;
  requires org.jspecify;
  requires java.naming;
}
