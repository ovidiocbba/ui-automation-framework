package com.ovidiomiranda.framework.core.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Class to return the ChromeDriver for Chrome browser mode Headless.
 *
 * @author Ovidio Miranda
 */
public class ChromeHeadless implements Browser {

  /**
   * {@inheritDoc}
   */
  @Override
  public WebDriver getBrowser() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver(getChromeOptions());
  }

  /**
   * Gets Chrome options and capabilities.
   *
   * @return ChromeOptions instance.
   */
  private ChromeOptions getChromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--headless");
    return options;
  }
}
