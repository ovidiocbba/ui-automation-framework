package com.ovidiomiranda.framework.core.driver;

import com.ovidiomiranda.framework.core.browser.Browser;
import com.ovidiomiranda.framework.core.browser.Chrome;
import com.ovidiomiranda.framework.core.browser.ChromeHeadless;
import com.ovidiomiranda.framework.core.browser.Edge;
import com.ovidiomiranda.framework.core.browser.EdgeHeadless;
import com.ovidiomiranda.framework.core.browser.Firefox;
import com.ovidiomiranda.framework.core.browser.FirefoxHeadless;
import java.util.EnumMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;

/**
 * Class created in order to recognize the Driver type.
 *
 * @author Ovidio Miranda
 */
public final class DriverFactory {

  private static final Map<DriverType, Browser> BROWSERS = new EnumMap<>(DriverType.class);

  /**
   * Private Constructor for the DriverFactory utility class.
   */
  private DriverFactory() {
  }

  static {
    BROWSERS.put(DriverType.CHROME, new Chrome());
    BROWSERS.put(DriverType.CHROME_HEADLESS, new ChromeHeadless());
    BROWSERS.put(DriverType.FIREFOX, new Firefox());
    BROWSERS.put(DriverType.FIREFOX_HEADLESS, new FirefoxHeadless());
    BROWSERS.put(DriverType.EDGE, new Edge());
    BROWSERS.put(DriverType.EDGE_HEADLESS, new EdgeHeadless());
  }

  /**
   * Return the WebDriver instance.
   *
   * @param driverType Enum value specified from DriverType
   * @return a WebDriver instance
   */
  public static WebDriver getDriverManager(final DriverType driverType) {
    Browser browser = BROWSERS.get(driverType);
    if (browser == null) {
      throw new IllegalArgumentException("Unsupported driver type: " + driverType);
    }
    return browser.getBrowser();
  }
}
