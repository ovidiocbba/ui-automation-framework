package com.ovidiomiranda.framework.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The PropertiesManager class to read data from gradle.properties.
 *
 * @author Ovidio Miranda
 */
public final class PropertiesManager {

  private static PropertiesManager propertiesManager;
  private final Properties properties;

  /**
   * Constructor for PropertiesManager.
   */
  private PropertiesManager() {
    properties = loadProperties();
  }

  /**
   * Loads properties from gradle.properties file located in classpath.
   *
   * @return Properties object containing configuration values
   * @throws RuntimeException if file is missing or cannot be loaded
   */
  private Properties loadProperties() {
    Properties props = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("gradle.properties")) {
      if (input == null) {
        throw new RuntimeException("gradle.properties not found");
      }
      props.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load properties", e);
    }
    return props;
  }

  /**
   * Gets the singleton instance of PropertiesManager.
   *
   * @return PropertiesManager instance
   */
  public static synchronized PropertiesManager getInstance() {
    if (propertiesManager == null) {
      propertiesManager = new PropertiesManager();
    }
    return propertiesManager;
  }

  /**
   * Gets property value based on enum key.
   *
   * @param key property enum
   * @return property value
   */
  public String getProperty(PropertiesInput key) {
    String systemValue = System.getProperty(key.getPropertiesName());
    return systemValue != null ? systemValue : properties.getProperty(key.getPropertiesName());
  }
}