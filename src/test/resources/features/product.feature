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
