package com.ovidiomiranda.framework.steps;

import com.ovidiomiranda.framework.pages.HomePage;
import com.ovidiomiranda.framework.pages.SearchResultsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

/**
 * Step definitions related to actions performed on the 'Home' page.
 *
 * @author Ovidio Miranda
 */
public class HomeSteps {

  private HomePage homePage;
  private SearchResultsPage searchResultsPage;

  /**
   * Initializes a new instance of 'HomeSteps' class.
   *
   * @param homePage the Home page instance, see {@link HomePage} class.
   */
  public HomeSteps(HomePage homePage) {
    this.homePage = homePage;
  }

  /**
   * Navigates to the 'Home' page.
   */
  @Given("I navigate to 'Home' page")
  public void navigate() {
    homePage.open();
  }

  /**
   * Enters the specified product name into the search field.
   *
   * @param product the product name to search for
   */
  @And("I enter {string} in the Search Field on 'Home' page")
  public void enterSearchProduct(String product) {
    homePage.enterSearchProduct(product);
  }

  /**
   * Clicks the 'Search' button.
   */
  @And("I click 'Search' button on 'Home' page")
  public void clickSearchButton() {
    searchResultsPage = homePage.clickSearchButton();
  }
}
