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
}
