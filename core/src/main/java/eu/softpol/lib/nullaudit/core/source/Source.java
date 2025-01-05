package eu.softpol.lib.nullaudit.core.source;

import eu.softpol.lib.nullaudit.core.analyzer.FileAnalyzer;

public interface Source {

  void analyze(FileAnalyzer fileAnalyzer);
}
