package com.ovidiomiranda.framework.hooks;

import com.ovidiomiranda.framework.core.driver.DriverManager;
import io.cucumber.java.After;

/**
 * Class containing Common Hooks.
 *
 * @author Ovidio Miranda
 */
public class CommonHooks {

  /**
   * Closes the WebDriver instance after each scenario execution.
   */
  @After
  public void tearDown() {
    DriverManager.getInstance().quitDriver();
  }
}
