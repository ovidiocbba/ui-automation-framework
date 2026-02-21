package com.ovidiomiranda.framework.steps;

import com.ovidiomiranda.framework.pages.SearchResultsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

/**
 * Step definitions related to actions performed on the 'SearchResults' page.
 *
 * @author Ovidio Miranda
 */
public class SearchResultsSteps {

  private final SearchResultsPage searchResultsPage;

  /**
   * Initializes a new instance of 'SearchResultsSteps' class.
   *
   * @param searchResultsPage the SearchResults page instance, see {@link SearchResultsPage} class.
   */
  public SearchResultsSteps(SearchResultsPage searchResultsPage) {
    this.searchResultsPage = searchResultsPage;
  }

  /**
   * Clicks on the given pagination page number.
   *
   * @param pageNumber page number to navigate to
   */
  @And("I click on page number {int}")
  public void clickOnPageNumber(int pageNumber) {
    searchResultsPage.clickOnPageNumber(pageNumber);
  }

  /**
   * Clicks on the 'Next' pagination button in search results.
   */
  @And("I click on the Next pagination button")
  public void clickOnTheNextPaginationButton() {
    searchResultsPage.clickOnNextPaginationButton();
  }

  /**
   * Verifies that the search results header is related to the expected product.
   *
   * @param product the expected product name
   */
  @Then("I should see search results related to {string}")
  public void shouldSeeSearchResultsRelatedTo(String product) {
    Assert.assertTrue(searchResultsPage.isHeaderRelatedTo(product));
  }

  /**
   * Verifies that the search results list is not empty.
   */
  @And("the results list should not be empty")
  public void theResultsListShouldNotBeEmpty() {
    Assert.assertFalse(searchResultsPage.isResultsListEmpty(),
        "Expected results list not to be empty");
  }

  /**
   * Verifies that the browser page title contains the expected product name.
   */
  @Then("the page title should contain {string}")
  public void pageTitleShouldContain(String product) {
    Assert.assertTrue(searchResultsPage.isTitleRelatedTo(product),
        "Expected page title to contain: " + product);
  }

  /**
   * Verifies that the current active pagination page matches the expected value.
   *
   * @param expectedPage expected active page number
   */
  @Then("the current page number should be {int}")
  public void currentPageNumberShouldBe(int expectedPage) {
    Assert.assertEquals(searchResultsPage.getCurrentActivePageNumber(), expectedPage,
        "Expected current page number to be: " + expectedPage);
  }

  /**
   * Verifies that the 'Previous' pagination button is visible.
   */
  @Then("the Previous pagination button should be visible")
  public void previousPaginationButtonShouldBeVisible() {
    Assert.assertTrue(searchResultsPage.isPreviousPaginationButtonVisible(),
        "Expected Previous pagination button to be visible");
  }

  /**
   * Verifies that at least one result title contains the searched keyword.
   *
   * @param keyword searched keyword
   */
  @Then("at least one result title should contain {string}")
  public void atLeastOneResultTitleShouldContain(String keyword) {
    Assert.assertTrue(searchResultsPage.doesAnyResultTitleContain(keyword),
        "No result titles contain keyword: " + keyword);
  }
}
