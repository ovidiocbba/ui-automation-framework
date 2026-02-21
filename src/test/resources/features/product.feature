Feature: Product Search
  As a customer
  I want to search for products
  So that I can view relevant search results

  Background:
    Given I navigate to 'Home' page

  @TC-00001 @regression
  Scenario Outline: Search for a valid product
    When I enter "<product>" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    Then I should see search results related to "<product>"
    And the results list should not be empty

    Examples:
      | product  |
      | laptop   |
      | keyboard |

  @TC-00002 @regression
  Scenario: Page title should contain searched product
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    Then the page title should contain "laptop"

  @TC-00003 @regression
  Scenario: User can navigate to second page of search results
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    And I click on page number 2
    Then the current page number should be 2

  @TC-00004 @regression
  Scenario: User can navigate using Next button
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    And I click on the Next pagination button
    Then the current page number should be 2

  @TC-00005 @regression
  Scenario: Previous button should appear on second page
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    And I click on page number 2
    Then the Previous pagination button should be visible

  @TC-00006 @regression
  Scenario: Search results should include the searched keyword in their titles
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    Then at least one result title should contain "laptop"

  @TC-00007 @regression
  Scenario: Previous button should be disabled on first page
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    Then the Previous pagination button should be disabled

  @TC-00008 @regression
  Scenario: The search field should have the correct placeholder text
    Given I navigate to 'Home' page
    Then the search field should display the placeholder text "Search Amazon"

  @TC-00009 @regression
  Scenario: Active page number should be 1 after performing a new search
    Given I navigate to 'Home' page
    When I enter "laptop" in the Search Field on 'Home' page
    And I click 'Search' button on 'Home' page
    Then the current page number should be 1
