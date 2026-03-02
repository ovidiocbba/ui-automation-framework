package com.ovidiomiranda.framework.core.browser;


import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Class to return the ChromeDriver for Chrome browser mode Headless.
 *
 * <p>This implementation includes additional configuration to reduce
 * browser automation detection by modern websites.
 *
 * @author Ovidio Miranda
 */
public class ChromeHeadless implements Browser {

  /**
   * Creates a headless ChromeDriver instance and hides automation signals.
   */
  @Override
  public WebDriver getBrowser() {
    ChromeOptions options = getChromeOptions();
    ChromeDriver driver = new ChromeDriver(options);
    // Overrides navigator.webdriver to prevent basic bot detection
    driver.executeCdpCommand(
        "Page.addScriptToEvaluateOnNewDocument",
        Map.of("source",
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
    );
    return driver;
  }

  /**
   * Configures Chrome options for CI stability and reduced automation detection.
   *
   * @return ChromeOptions instance.
   */
  private ChromeOptions getChromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    // Runs Chrome without UI using modern headless mode
    options.addArguments("--headless=new");
    // Sets realistic screen resolution to reduce fingerprinting
    options.addArguments("--window-size=1920,1080");

    // Disables Chrome automation flag used for bot detection
    options.addArguments("--disable-blink-features=AutomationControlled");
    // Removes Selenium's default automation switch
    options.setExperimentalOption("excludeSwitches",
        List.of("enable-automation"));
    // Disables Selenium automation extension
    options.setExperimentalOption("useAutomationExtension", false);
    return options;
  }
}
