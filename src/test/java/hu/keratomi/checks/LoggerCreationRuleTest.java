package hu.keratomi.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class LoggerCreationRuleTest {

    @Test
    public void shouldGenerateIssueIfLOGGERNotExists() {
        LoggerCreationRule rule = new LoggerCreationRule();

        JavaCheckVerifier.verify("src/test/files/LoggerCreationWithError.java", rule);
    }

    @Test
    public void shouldNotGenerateIssueIfLOGGERExists() {
        LoggerCreationRule rule = new LoggerCreationRule();
        rule.loggerFullyQualifiedName = "hu.keratomi.checks.Logger";

        JavaCheckVerifier.verifyNoIssue("src/test/files/LoggerCreationWithNoError.java", rule);
    }
}
