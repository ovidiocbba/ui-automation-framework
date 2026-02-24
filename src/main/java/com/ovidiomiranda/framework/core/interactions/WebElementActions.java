package com.ovidiomiranda.framework.core.interactions;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import com.ovidiomiranda.framework.core.waits.ExplicitWait;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that provides common web interaction methods.
 *
 * @author Ovidio Miranda
 */
public final class WebElementActions {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebElementActions.class);

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
    return DriverManager.getDriver();
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
    LOGGER.info("Entered text '{}' into element ({})", text, locator);
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
    LOGGER.info("Clicked on element ({})", locator);
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
    String text = getDriver().findElement(locator).getText();
    LOGGER.debug("Retrieved text '{}' from element located: {}", text, locator);
    return text;
  }

  /**
   * Gets the number of elements found for the given locator.
   *
   * @param locator the {@link By} locator
   * @return number of matching elements
   */
  public static int getElementsCount(By locator) {
    int count = getDriver().findElements(locator).size();
    LOGGER.debug("Found {} elements for locator: {}", count, locator);
    return count;
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

  /**
   * Retrieves the visible text from all elements matching the given locator.
   *
   * <p>Waits until at least one element is visible before collecting texts.
   *
   * @param locator the {@link By} locator of the elements
   * @return list of visible text values
   */
  public static List<String> getElementsText(final By locator) {
    ExplicitWait.waitUntilVisible(locator);
    return getDriver().findElements(locator).stream().map(WebElement::getText)
        .filter(text -> !text.isBlank()).collect(Collectors.toList());
  }
}
