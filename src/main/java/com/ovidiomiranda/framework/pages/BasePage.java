package com.ovidiomiranda.framework.pages;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import com.ovidiomiranda.framework.core.interactions.WebElementActions;
import com.ovidiomiranda.framework.core.waits.ExplicitWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class implementing the Page Object pattern.
 *
 * <p>Implements the Page Object Model (POM) design pattern by
 * providing a common WebDriver instance to all pages.
 *
 * @author Ovidio Miranda
 */
public abstract class BasePage {

  private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
  /**
   * WebDriver instance used to interact with the browser.
   */
  protected WebDriver driver;
  // Validation page locator
  private final By continueShoppingButton = By.xpath("//button[text()='Continue shopping']");

  /**
   * Initializes the WebDriver from DriverManager.
   */
  protected BasePage() {
    driver = DriverManager.getDriver();
  }

  /**
   * Defines the synchronization logic to confirm that the page is fully loaded and ready for
   * interaction.
   *
   * <p>This method must be implemented by each concrete page.
   */
  public abstract void waitUntilPageIsLoaded();

  /**
   * Checks if the current page is a validation page.
   *
   * @return true if validation page is detected
   */
  protected boolean isValidationPage() {
    return ExplicitWait.waitUntilVisibleShort(continueShoppingButton, 4);
  }

  /**
   * Attempts to bypass validation page automatically.
   *
   * <p>Only works if "Continue shopping" button exists.
   * Will fail if a full captcha challenge is displayed.
   */
  protected void bypassValidationIfPresent() {
    if (isValidationPage()) {
      LOGGER.info("Validation page detected. Attempting to continue.");
      try {
        WebElementActions.click(continueShoppingButton);
        LOGGER.info("Validation page handled successfully.");
      } catch (Exception e) {
        LOGGER.error("Could not bypass validation page.", e);
        throw new RuntimeException("Unable to automatically handle validation page.", e);
      }
    }
  }
}
