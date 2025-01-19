package eu.softpol.lib.nullaudit.core.report;

import java.util.List;

public record Issue(
    String location,
    List<Kind> kinds,
    String message
) {

}
