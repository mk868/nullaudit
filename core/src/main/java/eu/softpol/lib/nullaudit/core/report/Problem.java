package eu.softpol.lib.nullaudit.core.report;

import java.util.List;
import org.jspecify.annotations.Nullable;

public record Problem(
    @Nullable String module,
    String className,
    List<ProblemEntry> entries
) {

}
