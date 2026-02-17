package com.ovidiomiranda.framework.core.driver;

import com.ovidiomiranda.framework.core.browser.Browser;
import com.ovidiomiranda.framework.core.browser.Chrome;
import com.ovidiomiranda.framework.core.browser.Edge;
import com.ovidiomiranda.framework.core.browser.Firefox;
import java.util.EnumMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;

/**
 * Class created in order to recognize the Driver type.
 *
 * @author Ovidio Miranda
 */
public class DriverFactory {

  private static final Map<DriverType, Browser> BROWSERS = new EnumMap<>(DriverType.class);

  /**
   * Private Constructor for the DriverFactory utility class.
   */
  private DriverFactory() {
  }

  static {
    BROWSERS.put(DriverType.CHROME, new Chrome());
    BROWSERS.put(DriverType.FIREFOX, new Firefox());
    BROWSERS.put(DriverType.EDGE, new Edge());
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
