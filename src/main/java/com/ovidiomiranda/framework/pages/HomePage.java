package com.ovidiomiranda.framework.pages;

import com.ovidiomiranda.framework.core.config.PropertiesInput;
import com.ovidiomiranda.framework.core.config.PropertiesManager;
import com.ovidiomiranda.framework.core.interactions.WebElementActions;
import com.ovidiomiranda.framework.core.waits.ExplicitWait;
import org.openqa.selenium.By;

/**
 * Represents the 'Home' page and its components.
 *
 * @author Ovidio Miranda
 */
public class HomePage extends BasePage {

  private final String baseUrl = PropertiesManager.getInstance()
      .getProperty(PropertiesInput.BASE_URL);

  private final By searchTextField = By.id("twotabsearchtextbox");
  private final By searchButton = By.id("nav-search-submit-button");

  /**
   * Initializes a new instance of the HomePage class.
   */
  public HomePage() {
    super();
  }

  /**
   * Opens the base URL and waits for the page to load.
   *
   * @return current HomePage instance
   */
  public HomePage open() {
    driver.get(baseUrl);
    waitUntilPageIsLoaded();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void waitUntilPageIsLoaded() {
    ExplicitWait.waitUntilVisible(searchTextField);
  }

  /**
   * Enters a product name into the search field.
   *
   * @param product product name to search
   */
  public void enterSearchProduct(String product) {
    WebElementActions.enterTextField(searchTextField, product);
  }

  /**
   * Clicks the search button and navigates to the Search Results page.
   *
   * @return new SearchResultsPage instance
   */
  public SearchResultsPage clickSearchButton() {
    WebElementActions.click(searchButton);
    return new SearchResultsPage();
  }

  /**
   * Gets the placeholder text from the search input field.
   *
   * <p>This method retrieves the value of the "placeholder"
   * attribute from the search text field element.
   *
   * @return placeholder text of the search field
   */
  public String getSearchFieldPlaceholder() {
    return driver.findElement(searchTextField).getAttribute("placeholder");
  }
}