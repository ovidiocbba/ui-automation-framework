package com.ovidiomiranda.framework.core.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Class to return the ChromeDriver for Chrome browser.
 *
 * @author Ovidio Miranda
 */
public class Chrome implements Browser {

  /**
   * {@inheritDoc}
   */
  @Override
  public WebDriver getBrowser() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver();
  }
}

