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
}
