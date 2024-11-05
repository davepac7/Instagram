package com.ableto.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohammad Majid on 5/31/2020.
 */

public class DriverFactory {
	public enum BrowserType {
		CHROME, FIREFOX, CLOUD_CHROME, CLOUD_FIREFOX, CLOUD_IE, GRID_CHROME, GRID_FIREFOX, GRID_IE
	}

	private static DriverFactory instance = null;
	public static final String USERNAME = "";
	public static final String AUTOMATE_KEY = "";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	public static final String LOCAL_GRID_URL = "http://localhost:4444/wd/hub";

	private DriverFactory() {
		// Do-nothing..Do not allow to initialize this class from outside
	}

	public static DriverFactory getInstance() {
		if (instance == null) {
			instance = new DriverFactory();
		}
		return instance;
	}

	public static String getOperatingSystem() {
		String os = System.getProperty("os.name");
		return os;
	}

	public static DriverFactory getInstance(String browserName) {
		System.out.println("Running browser: " + browserName);
		
		if (instance == null) {
			instance = new DriverFactory();
		}

		if (browserName.equalsIgnoreCase("chrome")) {
			ChromeDriverManager.chromedriver().browserVersion("latest").setup();
			ChromeOptions options = new ChromeOptions();
//			if(getOperatingSystem().toLowerCase().equals("linux")) {
//				options.setHeadless(true);
//			}
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--disable-dev-shm-usage");
			//options.addArguments("user-agent=Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; Lumia 640 XL LTE) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Mobile Safari/537.36 Edge/12.10166");
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setExperimentalOption("useAutomationExtension", false);
			options.addArguments("--disable-blink-features=AutomationControlled");
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			options.addArguments("start-maximized");
			options.addArguments("--disable-web-security");
			options.addArguments("--no-proxy-server");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);

			options.setExperimentalOption("prefs", prefs);
			instance.driver.set(new ChromeDriver(options));
		} else if (browserName.equalsIgnoreCase("firefox")) {
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("marionette", false);
			System.out.println(dc.getCapabilityNames());
			//dc.setCapability();
//			dc.setPlatform(Platform.IOS);
			WebDriverManager.firefoxdriver().browserVersion("48").setup();
			instance.driver.set(new FirefoxDriver());
		} else if (browserName.equalsIgnoreCase("cloud_chrome")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("browser", "Chrome");
			caps.setCapability("browser_version", "64.0");
			caps.setCapability("os", "Windows");
			caps.setCapability("os_version", "7");
			caps.setCapability("resolution", "1920x1080");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("cloud_firefox")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("browser", "Firefox");
			caps.setCapability("os_version", "7");
			caps.setCapability("resolution", "1920x1080");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("cloud_ie")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("browser", "IE");
			caps.setCapability("browser_version", "11.0");
			caps.setCapability("os_version", "7");
			caps.setCapability("resolution", "1920x1080");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("grid_chrome")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setPlatform(Platform.ANY);
			caps.setBrowserName("chrome");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(LOCAL_GRID_URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("grid_firefox")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setPlatform(Platform.ANY);
			caps.setBrowserName("firefox");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(LOCAL_GRID_URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("grid_ie")) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setPlatform(Platform.ANY);
			caps.setBrowserName("internet explorer");
			try {
				instance.driver.set(new RemoteWebDriver(new URL(LOCAL_GRID_URL), caps));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() // thread local driver object for webdriver
	{
		@Override
		protected WebDriver initialValue() {
			FirefoxDriverManager.firefoxdriver().setup();
			return new FirefoxDriver();
		}
	};

	public WebDriver getDriver() // call this method to get the driver object and launch the browser
	{
		return driver.get();
	}

	public void removeDriver() // Quits the driver and closes the browser
	{		
		  driver.get().close(); 
		  //driver.get().quit();
		  driver.remove();	 
	}
}
