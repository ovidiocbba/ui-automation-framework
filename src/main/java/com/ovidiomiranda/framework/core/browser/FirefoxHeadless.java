package com.ovidiomiranda.framework.core.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Class to return the FirefoxDriver for Firefox browser mode Headless.
 *
 * <p>This implementation includes additional configuration to improve
 * CI stability and reduce basic automation detection signals.
 *
 * @author Ovidio Miranda
 */
public class FirefoxHeadless implements Browser {

  /**
   * Creates a headless FirefoxDriver instance.
   */
  @Override
  public WebDriver getBrowser() {
    FirefoxOptions options = getFirefoxOptions();
    return new FirefoxDriver(options);
  }

  /**
   * Configures Firefox options for CI stability and reduced automation detection.
   *
   * @return FirefoxOptions instance.
   */
  private FirefoxOptions getFirefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    // Enable headless mode
    options.addArguments("-headless");
    // Set realistic screen resolution
    options.addArguments("--width=1920");
    options.addArguments("--height=1080");
    // Create custom profile to tweak automation signals
    FirefoxProfile profile = new FirefoxProfile();
    // Attempts to reduce webdriver exposure
    profile.setPreference("dom.webdriver.enabled", false);
    profile.setPreference("useAutomationExtension", false);
    // Improves CI stability
    profile.setPreference("media.volume_scale", "0.0");
    options.setProfile(profile);
    return options;
  }
}
