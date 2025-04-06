package eu.softpol.lib.nullaudit.core.check;

import eu.softpol.lib.nullaudit.core.analyzer.NullScopeAnnotation;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedClass;
import eu.softpol.lib.nullaudit.core.analyzer.visitor.context.VisitedPackage;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.report.Kind;
import java.util.List;
import java.util.function.BiConsumer;

public class IrrelevantMarkedCheck implements Check {

  private static final List<NullScopeAnnotation> IRRELEVANT_ANNOTATIONS = List.of(
      NullScopeAnnotation.NULL_MARKED,
      NullScopeAnnotation.NULL_UNMARKED
  );
  private final MessageSolver messageSolver;

  public IrrelevantMarkedCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkModule() {
    // NOP
  }

  @Override
  public void checkPackage(VisitedPackage visitedPackage, BiConsumer<List<Kind>, String> addIssue) {
    if (visitedPackage.annotations().containsAll(IRRELEVANT_ANNOTATIONS)) {
      addIssue.accept(
          List.of(Kind.IRRELEVANT_ANNOTATION),
          messageSolver.issueIrrelevantAnnotationNullUnMarkedPackage()
      );
    }
  }

  @Override
  public void checkClass(VisitedClass visitedClass, AddIssue addIssue) {
    if (visitedClass.annotations().containsAll(IRRELEVANT_ANNOTATIONS)) {
      addIssue.addIssueForClass(
          List.of(Kind.IRRELEVANT_ANNOTATION),
          messageSolver.issueIrrelevantAnnotationNullUnMarkedClass()
      );
    }

    visitedClass.methods().forEach(visitedMethod -> {
      if (visitedMethod.annotations().containsAll(IRRELEVANT_ANNOTATIONS)) {
        addIssue.addIssueForMethod(
            visitedMethod.descriptiveMethodName(),
            List.of(Kind.IRRELEVANT_ANNOTATION),
            messageSolver.issueIrrelevantAnnotationNullUnMarkedMethod()
        );
      }
    });
  }
}
