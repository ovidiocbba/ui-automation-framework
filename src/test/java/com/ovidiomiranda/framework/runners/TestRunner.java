package com.ovidiomiranda.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test Runner class responsible for executing Cucumber feature files.
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.ovidiomiranda.framework",
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json"
    }
)
public class TestRunner extends AbstractTestNGCucumberTests {

}
