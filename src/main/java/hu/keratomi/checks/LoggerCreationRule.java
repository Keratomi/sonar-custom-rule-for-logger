package hu.keratomi.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Rule(key = "LoggerCreationIsMandatory", priority = Priority.MINOR)
public class LoggerCreationRule extends BaseTreeVisitor implements JavaFileScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerCreationRule.class);

    private static final String DEFAULT_VALUE_OF_LOGGER_INTERFACE_FULLY_QUALIFIED_NAME = "org.slf4j.Logger";
    private static final String DEFAULT_VALUE_OF_LOGGER_INSTANCE_NAME = "LOGGER";
    private static final String DEFAULT_VALUE_OF_MARKER_ANNOTATION_NAME = "NoLogger";

    private JavaFileScannerContext context;

    @RuleProperty(
            defaultValue = DEFAULT_VALUE_OF_LOGGER_INTERFACE_FULLY_QUALIFIED_NAME,
            description = "Fully qualified name of logger interface")
    protected String loggerFullyQualifiedName;

    @RuleProperty(
            defaultValue = DEFAULT_VALUE_OF_LOGGER_INSTANCE_NAME,
            description = "Fully qualified name of logger instance")
    protected String loggerInstanceName;

    @RuleProperty(
            defaultValue = DEFAULT_VALUE_OF_MARKER_ANNOTATION_NAME,
            description = "Name of annotation which marks class to ignore scan")
    protected String markerAnnotationName;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitVariable(VariableTree tree) {
        super.visitVariable(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (tree.is(Tree.Kind.CLASS) && tree.simpleName() != null) {
            boolean isIgnoredClass = tree.modifiers().annotations().stream()
                    .filter(annotation -> annotation.annotationType().is(Tree.Kind.IDENTIFIER))
                    .map(annotation -> ((IdentifierTree) annotation.annotationType()).name())
                    .anyMatch(annotationName -> markerAnnotationName.equals(annotationName));

            if (!isIgnoredClass) {

                List<VariableTree> loggerVariables = tree.members().stream()
                        .filter(member -> member instanceof VariableTree)
                        .map(member -> (VariableTree) member)
                        .filter(variable -> loggerInstanceName.equals(variable.simpleName().name()))
                        .filter(variable -> variable.type().symbolType().is(loggerFullyQualifiedName))
                        .filter(variable -> variable.modifiers().modifiers().stream()
                                .map(modifier -> modifier.modifier())
                                .collect(Collectors.toList())
                                .containsAll(Arrays.asList(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)))
                        .collect(Collectors.toList());

                if (loggerVariables.size() == 0) {
                    context.reportIssue(this, tree, String.format("%s not found in class %s", loggerInstanceName, tree.simpleName()));
                }
            }
        }

        super.visitClass(tree);
    }
}
