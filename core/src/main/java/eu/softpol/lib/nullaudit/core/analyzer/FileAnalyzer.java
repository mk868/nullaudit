package eu.softpol.lib.nullaudit.core.analyzer;

public interface FileAnalyzer {

  boolean analyze(String fileName, InputStreamSupplier is);
}
