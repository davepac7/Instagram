package com.ableto.base;

import com.ableto.utilities.*;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static com.ableto.base.BaseTest.xlPath;

public class PageBase extends Utilities {

	static final Integer DEFAULT_TIMEOUT = Integer.parseInt(System.getProperty("selenium.defaultTimeout", "5"));

	//public Properties prop = new Properties();
	protected LogBuilder logBuilder = new LogBuilder();

	public PageBase() {
		super();
	}

	protected String sheet = "Environment";

	public boolean isPageReady() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
			wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
					.equals("complete"));
		} catch (WebDriverException e) {
			return false;
		}
		return true;
	}

	// deprecated
	public void getAbleToApplicationUrl() {
		driver.get(ExcelFunctions.getCellValue(xlPath, sheet, 2, 1)); // Able to URL
	}

	public String getPreLoginUsername() {
		String username = ExcelFunctions.getCellValue(xlPath, sheet, 2, 4);
		return username;
	}

	public String getPreLoginPassword() {
		String password = ExcelFunctions.getCellValue(xlPath, sheet, 2, 5);
		return password;
	}

	// ------------------This is to pre login to the ableTo
	// application-------------------
	public void preLogin() {
		String username = getPreLoginUsername().toUpperCase();
		String password = getPreLoginPassword().toUpperCase();
		System.out.println(username);

		try {

			// This is to check the Caps lock key state and make it off if its on
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
				Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
			}

			enterText(username);
			tabPress();
			tabRelease();
			enterText(password);
			enterPress();
			enterRelease();

			DriverFactory.getInstance().getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
