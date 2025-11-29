package eu.softpol.lib.nullaudit.core.check.prohibit_non_jspecify_annotations;

import static eu.softpol.lib.nullaudit.core.check.CheckUtils.isDefaultRecordAccessorMethod;
import static eu.softpol.lib.nullaudit.core.check.CheckUtils.isDefaultRecordConstructor;
import static java.util.function.Predicate.not;

import eu.softpol.lib.nullaudit.core.check.ClassCheckContext;
import eu.softpol.lib.nullaudit.core.check.ClassChecker;
import eu.softpol.lib.nullaudit.core.i18n.MessageKey;
import eu.softpol.lib.nullaudit.core.i18n.MessageSolver;
import eu.softpol.lib.nullaudit.core.model.NAAnnotation;
import eu.softpol.lib.nullaudit.core.report.Kind;
import eu.softpol.lib.nullaudit.core.type.TypeNode;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProhibitNonJSpecifyAnnotationsCheck implements ClassChecker {

  private final MessageSolver messageSolver;

  public ProhibitNonJSpecifyAnnotationsCheck(MessageSolver messageSolver) {
    this.messageSolver = messageSolver;
  }

  @Override
  public void checkClass(ClassCheckContext context) {
    var naClass = context.naClass();

    var classAnnotations = findProhibitedAnnotations(naClass.annotations());
    if (!classAnnotations.isEmpty()) {
      context.addIssueForClass(
          Kind.PROHIBITED_ANNOTATION,
          messageSolver.resolve(MessageKey.ISSUE_PROHIBITED_ANNOTATION_CLASS,
              format(classAnnotations))
      );
    }

    naClass.components().forEach(component -> {
      var prohibited = new LinkedHashSet<NAAnnotation>();
      prohibited.addAll(findProhibitedAnnotations(component.annotations()));
      prohibited.addAll(findProhibitedAnnotations(component.type()));

      if (!prohibited.isEmpty()) {
        context.addIssueForComponent(
            component,
            Kind.PROHIBITED_ANNOTATION,
            messageSolver.resolve(MessageKey.ISSUE_PROHIBITED_ANNOTATION_COMPONENT,
                format(prohibited))
        );
      }
    });

    if (!naClass.isRecord()) {
      naClass.fields().forEach(field -> {
        var prohibited = new LinkedHashSet<NAAnnotation>();
        prohibited.addAll(findProhibitedAnnotations(field.annotations()));
        prohibited.addAll(findProhibitedAnnotations(field.type()));

        if (!prohibited.isEmpty()) {
          context.addIssueForField(
              field,
              Kind.PROHIBITED_ANNOTATION,
              messageSolver.resolve(MessageKey.ISSUE_PROHIBITED_ANNOTATION_FIELD,
                  format(prohibited))
          );
        }
      });
    } else {
      // TODO static fields
    }

    naClass.methods().stream()
        .filter(not(m -> isDefaultRecordConstructor(naClass, m)))
        .filter(not(m -> isDefaultRecordAccessorMethod(naClass, m)))
        .forEach(method -> {
          var prohibited = new LinkedHashSet<NAAnnotation>();
          prohibited.addAll(findProhibitedAnnotations(method.annotations()));
          prohibited.addAll(findProhibitedAnnotations(method.returnType()));

          for (var param : method.parameters()) {
            prohibited.addAll(findProhibitedAnnotations(param.annotations()));
            prohibited.addAll(findProhibitedAnnotations(param.type()));
          }

          if (!prohibited.isEmpty()) {
            context.addIssueForMethod(
                method,
                Kind.PROHIBITED_ANNOTATION,
                messageSolver.resolve(MessageKey.ISSUE_PROHIBITED_ANNOTATION_METHOD,
                    format(prohibited))
            );
          }
        });
  }

  private static Set<NAAnnotation> findProhibitedAnnotations(Collection<NAAnnotation> annotations) {
    return annotations.stream()
        .filter(ProhibitedAnnotations.ALL::contains)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private static Set<NAAnnotation> findProhibitedAnnotations(TypeNode typeNode) {
    var result = new LinkedHashSet<>(findProhibitedAnnotations(typeNode.getAnnotations()));
    for (TypeNode child : typeNode.getChildren()) {
      result.addAll(findProhibitedAnnotations(child));
    }
    return result;
  }

  private static String format(Set<NAAnnotation> annotations) {
    return annotations.stream()
        .map(NAAnnotation::fqcn)
        .collect(Collectors.joining(", "));
  }
}
