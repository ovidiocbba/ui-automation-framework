package com.ovidiomiranda.framework.utils;

import static java.lang.String.format;

import io.cucumber.java.Scenario;

/**
 * All support functions related with scenario actions.
 *
 * @author Ovidio Miranda
 */
public final class ScenarioUtils {

  /**
   * Private constructor to prevent instantiation.
   */
  private ScenarioUtils() {
    throw new UnsupportedOperationException("ScenarioUtils class and cannot be instantiated");
  }

  /**
   * Gets the Test Case ID from the scenario tags.
   *
   * <p>Example tag: {@code @TC-00003}
   *
   * @param scenario the current Cucumber scenario
   * @return the Test Case ID without the '@' symbol, or "NO-TC-ID" if not present
   */
  public static String getTestCaseId(final Scenario scenario) {
    final String pbiId = scenario.getSourceTagNames().stream().filter(tag -> tag.startsWith("@TC-"))
        .findFirst().orElse("@NO-TC-ID");
    return pbiId.replace("@", "");
  }

  /**
   * Builds a formatted scenario title including Test Case ID and scenario name.
   *
   * <p>Example output:
   * <pre>
   * TC-00003 - Search for a valid product
   * </pre>
   *
   * @param scenario the current Cucumber scenario
   * @return formatted scenario title
   */
  public static String getTestCaseTitle(final Scenario scenario) {
    final String pbiTitle = scenario.getName();
    return format("%s - %s", getTestCaseId(scenario), pbiTitle);
  }
}

