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
        "json:build/cucumber.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "rerun:build/rerun.txt"
    }
)
public class TestRunner extends AbstractTestNGCucumberTests {

}
