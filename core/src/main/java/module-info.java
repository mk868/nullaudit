import org.jspecify.annotations.NullMarked;

@NullMarked
module eu.softpol.lib.nullaudit.core {
  exports eu.softpol.lib.nullaudit.core;
  exports eu.softpol.lib.nullaudit.core.report;
  // tests
  exports eu.softpol.lib.nullaudit.core.comparator to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.i18n to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.signature to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.type to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.type.translator to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.model to eu.softpol.lib.nullaudit.coretest;
  exports eu.softpol.lib.nullaudit.core.matcher to eu.softpol.lib.nullaudit.coretest;

  requires org.objectweb.asm;
  requires org.jspecify;
  requires java.naming;
  requires org.immutables.value;
}
