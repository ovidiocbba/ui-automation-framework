package com.ovidiomiranda.framework.core.waits;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
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
    return DriverManager.getWebDriverWait();
  }

  /**
   * Creates a new {@link WebDriverWait} instance with a custom timeout.
   *
   * @param seconds timeout duration in seconds
   * @return WebDriverWait with custom timeout
   */
  private static WebDriverWait getWait(int seconds) {
    return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
  }

  /**
   * Waits for a short, custom period until the element becomes visible.
   *
   * <p>Intended for optional or conditional elements that may or may not appear,
   * such as popups or validation pages.
   *
   * @param locator the {@link By} locator of the element
   * @param seconds maximum time to wait in seconds
   * @return {@code true} if the element becomes visible; {@code false} if timeout occurs
   */
  public static boolean waitUntilVisibleShort(final By locator, final int seconds) {
    try {
      WebDriverWait shortWait = getWait(seconds);
      shortWait.pollingEvery(Duration.ofMillis(300));
      shortWait.ignoring(NoSuchElementException.class);
      shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }
}
