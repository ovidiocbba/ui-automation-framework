package com.ovidiomiranda.framework.core.waits;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class that provides reusable explicit wait operations.
 *
 * @author Ovidio Miranda
 */
public final class ExplicitWait {

  /**
   * Private constructor to prevent instantiation.
   */
  private ExplicitWait() {
  }

  /**
   * Waits until the element located by the given locator is visible.
   *
   * @param locator the {@link By} locator of the element
   */
  public static void waitUntilVisible(final By locator) {
    getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  /**
   * Waits until the element located by the given locator is clickable (visible and enabled).
   *
   * @param locator the {@link By} locator of the element
   */
  public static void waitUntilClickable(final By locator) {
    getWait().until(elementToBeClickable(locator));
  }

  /**
   * Gets configured {@link WebDriverWait} instance from the DriverManager.
   *
   * @return WebDriverWait instance
   */
  private static WebDriverWait getWait() {
    return DriverManager.getInstance().getWebDriverWait();
  }
}
