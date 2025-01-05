package eu.softpol.lib.nullaudit.core.report;

import java.util.List;

public record Report(
    List<Problem> problems
) {

}
