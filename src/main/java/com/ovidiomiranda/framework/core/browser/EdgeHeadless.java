package com.ovidiomiranda.framework.core.browser;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Class to return the EdgeDriver for Microsoft Edge browser mode Headless.
 *
 * <p>This implementation includes additional configuration to reduce
 * browser automation detection by modern websites and improve CI stability.
 *
 * @author Ovidio Miranda
 */
public class EdgeHeadless implements Browser {

  /**
   * Creates a headless EdgeDriver instance and hides automation signals.
   */
  @Override
  public WebDriver getBrowser() {
    EdgeOptions options = getEdgeOptions();
    EdgeDriver driver = new EdgeDriver(options);
    // Overrides navigator.webdriver to prevent basic bot detection
    driver.executeCdpCommand(
        "Page.addScriptToEvaluateOnNewDocument",
        Map.of("source",
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
    );
    return driver;
  }

  /**
   * Configures Edge options for CI stability and reduced automation detection.
   *
   * @return EdgeOptions instance.
   */
  private EdgeOptions getEdgeOptions() {
    EdgeOptions options = new EdgeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    // Runs Edge without UI using modern headless mode
    options.addArguments("--headless=new");
    // Sets realistic screen resolution to reduce fingerprinting
    options.addArguments("--window-size=1920,1080");
    // Disables automation flag used for bot detection
    options.addArguments("--disable-blink-features=AutomationControlled");
    // Removes Selenium's default automation switch
    options.setExperimentalOption("excludeSwitches",
        List.of("enable-automation"));
    // Disables Selenium automation extension
    options.setExperimentalOption("useAutomationExtension", false);
    return options;
  }
}
