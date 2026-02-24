package com.ovidiomiranda.framework.core.browser;

import org.openqa.selenium.WebDriver;

/**
 * Interface that defines how a browser WebDriver should be created.
 *
 * <p>Each browser must implement this interface. It helps separate browser
 * creation logic from the rest of the framework and is part of the Strategy Pattern.
 *
 * @author Ovidio Miranda
 */
public interface Browser {

  /**
   * Creates and returns a WebDriver instance.
   *
   * @return a WebDriver instance for the specified browser
   */
  WebDriver getBrowser();
}
