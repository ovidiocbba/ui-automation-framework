package com.ovidiomiranda.framework.core.interactions;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import com.ovidiomiranda.framework.core.waits.ExplicitWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class that provides common web interaction methods.
 *
 * @author Ovidio Miranda
 */
public final class WebElementActions {

  /**
   * Private constructor to prevent instantiation.
   */
  private WebElementActions() {
  }

  /**
   * Gets the active WebDriver instance from DriverManager.
   *
   * @return active WebDriver instance
   */
  private static WebDriver getDriver() {
    return DriverManager.getInstance().getDriver();
  }

  /**
   * Clears and enters text into a web locator.
   *
   * <p>Waits until the element is clickable before interacting.
   *
   * @param locator the {@link By} locator of the input field
   * @param text    text value to enter
   */
  public static void enterTextField(final By locator, final String text) {
    ExplicitWait.waitUntilClickable(locator);
    WebElement element = getDriver().findElement(locator);
    element.clear();
    element.sendKeys(text);
  }

  /**
   * Clicks on the element located by the given locator.
   *
   * <p>Waits until the element is clickable before performing the click.
   *
   * @param locator the {@link By} locator of the element
   */
  public static void click(final By locator) {
    ExplicitWait.waitUntilClickable(locator);
    getDriver().findElement(locator).click();
  }

  /**
   * Gets the visible text of the element located by the given locator.
   *
   * <p>Waits until the element is visible before retrieving the text.
   *
   * @param locator the {@link By} locator of the element
   * @return the visible text of the element
   */
  public static String getText(By locator) {
    ExplicitWait.waitUntilVisible(locator);
    return getDriver().findElement(locator).getText();
  }

  /**
   * Gets the number of elements found for the given locator.
   *
   * @param locator the {@link By} locator
   * @return number of matching elements
   */
  public static int getElementsCount(By locator) {
    return getDriver().findElements(locator).size();
  }

  /**
   * Determines whether the element located by the given locator is displayed.
   *
   * @param locator the {@link By} locator of the element
   * @return true if the element is displayed; false otherwise
   */
  public static boolean isElementDisplayed(final By locator) {
    try {
      ExplicitWait.waitUntilVisible(locator);
      return getDriver().findElement(locator).isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }
}
