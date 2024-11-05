package com.instagram.base;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.instagram.pages.LoginPage;
import com.instagram.pages.MainPage;
import com.instagram.utilities.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseTest {

    public static JavascriptExecutor js;
    public ExtentTest test;
    protected WebDriver driver;
    protected Utilities utilities;
    protected LoginPage loginPage;
    protected MainPage mainPage;
    protected PageBase pageBase;
    protected LogBuilder logBuilder;
    protected String env;
    protected String url;
    protected ExtentReporter extent;


    // -----------------------Launch browser-------------------------
    // browserName = chrome,firefox,ie,grid_chrome,grid_firefox,grid_ie
    @Parameters("browserName")
    @BeforeClass(alwaysRun = true)
    public void beforeClass(@Optional(value = ("firefox")) String browserName) {
        driver = DriverFactory.getInstance(browserName).getDriver();
        driver.manage().window().maximize();

        logBuilder = new LogBuilder();
        extent = new ExtentReporter();
        pageBase = new PageBase();
        utilities = new Utilities();
        loginPage = new LoginPage();

        String logFolder = "logs";
        File logFile = new File(logFolder);
        try {
            if (logFile.exists()) {
                FileUtils.deleteDirectory(logFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.get("https://www.instagram.com");


    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        try {
            driver.get(url);
        } catch (Exception e) {
        }
    }

//	-----------------------Tear down-------------------------

    @AfterClass(alwaysRun = true)
    public void tearDown() throws IOException {
        String path="cmd /c start C:\\Users\\Amira\\Documents\\killFirefox.bat";

        try{
            Process p = Runtime.getRuntime().exec(path);
            p.waitFor();

        }catch( IOException ex ){
            //Validate the case the file can't be accesed (not enought permissions)

        }catch( InterruptedException ex ){
            //Validate the case the process is being stopped by some external situation

        }
        pageBase = null;
        utilities = null;
        loginPage = null;
        mainPage = null;
        DriverFactory.getInstance().removeDriver();
        driver = null;
    }
}
