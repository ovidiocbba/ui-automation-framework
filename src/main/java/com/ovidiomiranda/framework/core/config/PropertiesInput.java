package com.ovidiomiranda.framework.core.config;

/**
 * Enum that defines the supported configuration keys used by the framework.
 *
 * @author Ovidio Miranda
 */
public enum PropertiesInput {
  /**
   * Browser type configuration key (e.g., CHROME, EDGE, FIREFOX).
   */
  BROWSER("browser"),
  /**
   * Base URL used as the starting point for test execution.
   */
  BASE_URL("baseUrl"),
  /**
   * Explicit wait timeout value in seconds.
   */
  EXPLICIT_WAIT("explicitWait");

  private final String propertiesName;

  /**
   * Creates a configuration key mapping.
   *
   * @param propertiesName the property key name defined in configuration
   */
  PropertiesInput(final String propertiesName) {
    this.propertiesName = propertiesName;
  }

  /**
   * Gets the property key name.
   *
   * @return propertiesName the String enum
   */
  public String getPropertiesName() {
    return propertiesName;
  }
}
