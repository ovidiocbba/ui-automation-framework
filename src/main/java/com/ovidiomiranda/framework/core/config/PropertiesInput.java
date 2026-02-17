package com.ovidiomiranda.framework.core.config;

/**
 * Enum that defines the supported configuration keys used by the framework.
 *
 * @author Ovidio Miranda
 */
public enum PropertiesInput {
  BROWSER("browser"),
  BASE_URL("baseUrl"),
  EXPLICIT_WAIT("explicitWait");

  private String propertiesName;

  /**
   * Creates a configuration key mapping.
   *
   * @param propertiesName name
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
