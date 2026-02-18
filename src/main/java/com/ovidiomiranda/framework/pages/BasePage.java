package com.ovidiomiranda.framework.pages;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import org.openqa.selenium.WebDriver;

/**
 * Abstract base class implementing the Page Object pattern.
 *
 * <p>Implements the Page Object Model (POM) design pattern by
 * providing a common WebDriver instance to all pages.
 *
 * @author Ovidio Miranda
 */
public abstract class BasePage {

  protected WebDriver driver;

  /**
   * Initializes the WebDriver from DriverManager.
   */
  protected BasePage() {
    driver = DriverManager.getInstance().getDriver();
  }

  /**
   * Defines the synchronization logic to confirm that the page is fully loaded and ready for
   * interaction.
   *
   * <p>This method must be implemented by each concrete page.
   */
  public abstract void waitUntilPageIsLoaded();
}

