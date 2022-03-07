package com.ableto.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.ableto.pages.LoginPage;
import com.ableto.pages.claimsAdmin.ClaimsAdminPage;
import com.ableto.utilities.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseTest {

    public static String testname;
    public static String xlPath = "./testdata/Ableto_data.xlsx";
    public static JavascriptExecutor js;
    public ExtentTest test;
    protected WebDriver driver;
    protected Utilities utilities;
    protected LoginPage loginPage;
    protected PageBase pageBase;
    protected Properties prop;
    protected LogBuilder logBuilder;
    protected String env;
    protected String url;
    protected ExtentReporter extent;

    //RCM pod
    protected ClaimsAdminPage claimsAdminPage;

    // -----------------------Launch browser-------------------------
    // browserName = chrome,firefox,ie,grid_chrome,grid_firefox,grid_ie
    @Parameters("browserName")
    @BeforeClass(alwaysRun = true)
    public void beforeClass(@Optional(value = ("firefox")) String browserName) {
        driver = DriverFactory.getInstance(browserName).getDriver();
        //driver = new FirefoxDriver();
        driver.manage().window().maximize();

        // todo: we should check if they've already been initialized?
//        ConfigManager.getInstance().initializeProperties(env);
//        ConfigManager.getInstance().setProperty("env", env);
       // url = ConfigManager.getInstance().getString("url");

        logBuilder = new LogBuilder();

       // logBuilder.info("URL " + url);
        
        extent = new ExtentReporter();
        pageBase = new PageBase();
        utilities = new Utilities();
        loginPage = new LoginPage();
        claimsAdminPage = new ClaimsAdminPage();

        //logBuilder.info("Thread id " + Thread.currentThread().getId());

        String logFolder = "logs";
        File logFile = new File(logFolder);
        try {
            if (logFile.exists()) {
                FileUtils.deleteDirectory(logFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String srcFile = "logs" + File.separator + browserName + "_" + env + "_" + Thread.currentThread().getId();
        File logfile = new File(srcFile);
        logfile.mkdirs();

        ThreadContext.put("ROUTINGKEY", srcFile);
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
            logBuilder.error("Error: " + e.getStackTrace());
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
        claimsAdminPage = null;
        DriverFactory.getInstance().removeDriver();
        driver = null;
    }
}
