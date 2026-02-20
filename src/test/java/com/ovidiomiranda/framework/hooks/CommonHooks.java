package com.ovidiomiranda.framework.hooks;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Class containing Common Hooks.
 *
 * @author Ovidio Miranda
 */
public class CommonHooks {

  /**
   * After hook: attaches screenshot on failure and closes WebDriver.
   *
   * @param scenario current scenario
   */
  @After
  public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
      byte[] screenshot = ((TakesScreenshot) DriverManager.getInstance()
          .getDriver()).getScreenshotAs(OutputType.BYTES);
      Allure.addAttachment(scenario.getName(), new ByteArrayInputStream(screenshot));
    }
    DriverManager.getInstance().quitDriver();
  }
}
