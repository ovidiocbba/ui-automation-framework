package com.ovidiomiranda.framework.hooks;

import static com.ovidiomiranda.framework.utils.ScenarioUtils.getTestCaseId;
import static com.ovidiomiranda.framework.utils.ScenarioUtils.getTestCaseTitle;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class containing Common Hooks.
 *
 * @author Ovidio Miranda
 */
public class CommonHooks {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonHooks.class);
  private long startTime;
  private static final String SEPARATOR =
      "============================================================";

  /**
   * Initializes execution timing and logs scenario start.
   *
   * @param scenario the scenario to be executed
   */
  @Before(order = -1)
  public void beforeScenario(final Scenario scenario) {
    startTime = System.currentTimeMillis();
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(SEPARATOR);
      LOGGER.info("▶ SCENARIO STARTED | {}", getTestCaseTitle(scenario));
      LOGGER.info(SEPARATOR);
      LOGGER.info("");
    }
  }

  /**
   * Logs scenario result, attaches screenshot on failure, and closes the WebDriver instance.
   *
   * @param scenario the executed scenario
   */
  @After
  public void afterScenario(Scenario scenario) {
    String testCaseId = getTestCaseId(scenario);

    long durationMs = System.currentTimeMillis() - startTime;
    double durationSec = durationMs / 1000.0;

    String formattedDuration = String.format("%.2f", durationSec);

    if (scenario.isFailed()) {
      LOGGER.error("✖ SCENARIO FAILED | {} | {} s", testCaseId, formattedDuration);
      attachScreenshot(testCaseId);
    } else {
      LOGGER.info("✔ SCENARIO PASSED | {} | {} s", testCaseId, formattedDuration);
    }
    DriverManager.quitDriver();
  }

  /**
   * Captures and attaches a screenshot to the Allure report.
   *
   * @param scenarioTitle the attachment name
   */
  private void attachScreenshot(String scenarioTitle) {
    try {
      byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(
          OutputType.BYTES);

      Allure.addAttachment(scenarioTitle, new ByteArrayInputStream(screenshot));

      LOGGER.info("Screenshot attached to Allure report");

    } catch (Exception e) {
      LOGGER.error("Screenshot capture failed", e);
    }
  }
}
