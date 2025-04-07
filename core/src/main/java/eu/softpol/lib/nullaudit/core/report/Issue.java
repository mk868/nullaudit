package eu.softpol.lib.nullaudit.core.report;

public record Issue(
    String location,
    Kind kind,
    String message
) {

}
