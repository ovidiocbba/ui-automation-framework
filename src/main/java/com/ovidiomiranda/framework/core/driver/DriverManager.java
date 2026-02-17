package com.ovidiomiranda.framework.core.driver;

import com.ovidiomiranda.framework.core.config.PropertiesInput;
import com.ovidiomiranda.framework.core.config.PropertiesManager;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * DriverManager class that applies Singleton pattern to instance WebDriver only once.
 *
 * @author Ovidio Miranda
 */
public final class DriverManager {

  private static DriverManager driverManager;

  private WebDriver driver;
  private WebDriverWait wait;

  /**
   * Private constructor to avoid direct instantiation.
   */
  private DriverManager() {
  }

  /**
   * Gets singleton instance.
   *
   * @return DriverManager instance
   */
  public static DriverManager getInstance() {
    if (driverManager == null) {
      driverManager = new DriverManager();
    }
    return driverManager;
  }

  /**
   * Initializes WebDriver using configuration values.
   */
  private void initDriver() {
    DriverType driverType = DriverType.valueOf(
        PropertiesManager.getInstance().getProperty(PropertiesInput.BROWSER).toUpperCase());

    driver = DriverFactory.getDriverManager(driverType);
    // Maximize browser window
    driver.manage().window().maximize();
    // Explicit wait configuration
    long timeout = Long.parseLong(
        PropertiesManager.getInstance().getProperty(PropertiesInput.EXPLICIT_WAIT));
    wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
  }

  /**
   * Gets WebDriver instance.
   *
   * @return WebDriver instance
   */
  public WebDriver getDriver() {
    if (driver == null) {
      initDriver();
    }
    return driver;
  }

  /**
   * Gets WebDriverWait instance.
   *
   * @return WebDriverWait instance
   */
  public WebDriverWait getWebDriverWait() {
    return wait;
  }

  /**
   * Quit driver and clean resources.
   */
  public void quitDriver() {
    try {
      if (driver != null) {
        driver.quit();
      }
    } finally {
      driver = null;
      wait = null;
    }
  }
}
