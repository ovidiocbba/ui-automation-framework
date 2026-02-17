package com.ovidiomiranda.framework.core.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

/**
 * Class to return the EdgeDriver for Edge browser.
 *
 * @author Ovidio Miranda
 */
public class Edge implements Browser {

  /**
   * {@inheritDoc}
   */
  @Override
  public WebDriver getBrowser() {
    WebDriverManager.edgedriver().setup();
    return new EdgeDriver();
  }
}
