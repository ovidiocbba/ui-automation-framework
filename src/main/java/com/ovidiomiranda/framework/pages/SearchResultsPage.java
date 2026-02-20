package com.ovidiomiranda.framework.pages;

import com.ovidiomiranda.framework.core.interactions.WebElementActions;
import com.ovidiomiranda.framework.core.waits.ExplicitWait;
import org.openqa.selenium.By;

/**
 * Represents the 'SearchResults' page and its components.
 *
 * @author Ovidio Miranda
 */
public class SearchResultsPage extends BasePage {

  private final By header = By.cssSelector("span.a-color-state");
  private final By resultsList = By.cssSelector("div[data-component-type='s-search-result']");
  private final By activePageNumber = By.cssSelector(
      "span.s-pagination-item.s-pagination-selected");
  private final By nextPaginationButton = By.cssSelector("a.s-pagination-next");

  /**
   * Initializes a new instance of the 'SearchResultsPage' class.
   */
  public SearchResultsPage() {
    super();
    waitUntilPageIsLoaded();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void waitUntilPageIsLoaded() {
    ExplicitWait.waitUntilVisible(header);
  }

  /**
   * Builds the locator for a specific pagination page number.
   *
   * @param pageNumber page number in pagination
   * @return By locator for the page number link
   */
  private By pageNumberButton(int pageNumber) {
    return By.xpath("//a[text()='" + pageNumber + "']");
  }

  /**
   * Clicks on the given page number in pagination.
   *
   * @param pageNumber page number to click
   */
  public void clickOnPageNumber(int pageNumber) {
    WebElementActions.click(pageNumberButton(pageNumber));
  }

  /**
   * Clicks on the 'Next' pagination button.
   */
  public void clickOnNextPaginationButton() {
    ExplicitWait.waitUntilVisible(nextPaginationButton);
    WebElementActions.click(nextPaginationButton);
  }

  /**
   * Gets the currently active page number.
   *
   * @return active page number as integer
   */
  public int getCurrentActivePageNumber() {
    ExplicitWait.waitUntilVisible(activePageNumber);
    String activeText = driver.findElement(activePageNumber).getText();
    return Integer.parseInt(activeText);
  }

  /**
   * Validates whether the results list is empty.
   *
   * @return true if no results are found, false otherwise
   */
  public boolean isResultsListEmpty() {
    return WebElementActions.getElementsCount(resultsList) == 0;
  }

  /**
   * Validates whether the search results header contains the expected product name.
   *
   * @param product expected product name
   * @return true if header contains the product name
   */
  public boolean isHeaderRelatedTo(String product) {
    String headerText = WebElementActions.getText(header).toLowerCase();
    return headerText.contains(product.toLowerCase());
  }

  /**
   * Validates whether the browser page title contains the expected product name.
   *
   * @param product expected product name
   * @return true if page title contains product
   */
  public boolean isTitleRelatedTo(String product) {
    return driver.getTitle().toLowerCase().contains(product.toLowerCase());
  }
}
