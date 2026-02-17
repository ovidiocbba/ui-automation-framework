package com.ovidiomiranda.framework.core.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Class to return the FirefoxDriver for Firefox browser.
 *
 * @author Ovidio Miranda
 */
public class Firefox implements Browser {

  /**
   * {@inheritDoc}
   */
  @Override
  public WebDriver getBrowser() {
    WebDriverManager.firefoxdriver().setup();
    return new FirefoxDriver();
  }
}
