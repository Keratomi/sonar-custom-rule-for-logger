package hu.keratomi;/*
 * SonarQube Java Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import org.junit.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction.Type;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static org.fest.assertions.Assertions.assertThat;

public class MyJavaRulesDefinitionTest {

  @Test
  public void test() {
    JavaRulesDefinitionForLogger rulesDefinition = new JavaRulesDefinitionForLogger();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    Repository repository = context.repository(JavaRulesDefinitionForLogger.REPOSITORY_KEY);

    assertThat(repository.name()).isEqualTo("Custom Logger Rules Repository");
    assertThat(repository.language()).isEqualTo("java");
    assertThat(repository.rules()).hasSize(RulesList.getChecks().size());

    assertRuleProperties(repository);
    assertParameterProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }

  private void assertParameterProperties(Repository repository) {
    Param loggerFullyQualifiedName = repository.rule("LoggerCreationIsMandatory").param("loggerFullyQualifiedName");
    assertThat(loggerFullyQualifiedName).isNotNull();
    assertThat(loggerFullyQualifiedName.defaultValue()).isEqualTo("org.slf4j.Logger");
    assertThat(loggerFullyQualifiedName.description()).isEqualTo("Fully qualified name of logger interface");
    assertThat(loggerFullyQualifiedName.type()).isEqualTo(RuleParamType.STRING);

    Param loggerInstanceName = repository.rule("LoggerCreationIsMandatory").param("loggerInstanceName");
    assertThat(loggerInstanceName).isNotNull();
    assertThat(loggerInstanceName.defaultValue()).isEqualTo("LOGGER");
    assertThat(loggerInstanceName.description()).isEqualTo("Fully qualified name of logger instance");
    assertThat(loggerInstanceName.type()).isEqualTo(RuleParamType.STRING);

    Param markerAnnotationName = repository.rule("LoggerCreationIsMandatory").param("markerAnnotationName");
    assertThat(markerAnnotationName).isNotNull();
    assertThat(markerAnnotationName.defaultValue()).isEqualTo("NoLogger");
    assertThat(markerAnnotationName.description()).isEqualTo("Name of annotation which marks class to ignore scan");
    assertThat(markerAnnotationName.type()).isEqualTo(RuleParamType.STRING);
  }

  private void assertRuleProperties(Repository repository) {
    Rule rule = repository.rule("LoggerCreationIsMandatory");
    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Find a logger definition in the class");
    assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(rule.type()).isEqualTo(RuleType.BUG);
  }

  private void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }

}
