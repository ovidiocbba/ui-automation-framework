package com.ovidiomiranda.framework.core.driver;

import com.ovidiomiranda.framework.core.config.PropertiesInput;
import com.ovidiomiranda.framework.core.config.PropertiesManager;
import java.time.Duration;
import java.util.Locale;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * DriverManager class that applies Singleton pattern to instance WebDriver only once.
 *
 * @author Ovidio Miranda
 */
public final class DriverManager {
  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
  private static final ThreadLocal<WebDriverWait> WAIT = new ThreadLocal<>();

  /**
   * Private constructor to avoid direct instantiation.
   */
  private DriverManager() {
  }

  /**
   * Initializes WebDriver using configuration values.
   */
  private static void initDriver() {
    DriverType driverType = DriverType.valueOf(
        PropertiesManager.getInstance().getProperty(PropertiesInput.BROWSER)
            .toUpperCase(Locale.ENGLISH));

    WebDriver driver = DriverFactory.getDriverManager(driverType);
    driver.manage().window().maximize();

    long timeout = Long.parseLong(
        PropertiesManager.getInstance().getProperty(PropertiesInput.EXPLICIT_WAIT));

    DRIVER.set(driver);
    WAIT.set(new WebDriverWait(driver, Duration.ofSeconds(timeout)));
  }

  /**
   * Gets WebDriver instance.
   *
   * @return WebDriver instance
   */
  public static WebDriver getDriver() {
    if (DRIVER.get() == null) {
      initDriver();
    }
    return DRIVER.get();
  }

  /**
   * Gets WebDriverWait instance.
   *
   * @return WebDriverWait instance
   */
  public static WebDriverWait getWebDriverWait() {
    return WAIT.get();
  }

  /**
   * Quit driver and clean resources.
   */
  public static void quitDriver() {
    try {
      if (DRIVER.get() != null) {
        DRIVER.get().quit();
      }
    } finally {
      DRIVER.remove();
      WAIT.remove();
    }
  }
}
