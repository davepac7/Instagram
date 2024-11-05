package com.instagram.utilities;

import com.github.javafaker.Faker;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Thread.sleep;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Utilities {

    public WebDriver driver = DriverFactory.getInstance().getDriver();
    static ArrayList<String> win = new ArrayList<>();
    protected static ExtentReporter extent = new ExtentReporter();
    public static Random random = new Random();
    public static Robot robot;
    public Faker faker = new Faker();
    public LogBuilder logBuilder = new LogBuilder();
    public int time;

    protected JavascriptExecutor js = (JavascriptExecutor) driver;

    public void openNewTab() {
        js.executeScript("window.open()");
    }

    public void switchTab() {
        explicitWaitForWindows(2);
        switchToWindow(2);
    }

    private void write(final String s, String fileName) throws IOException {
        Files.writeString(
                Path.of(System.getProperty("java.io.tmpdir"), fileName),
                s + System.lineSeparator(),
                CREATE, APPEND
        );
    }

    public void switchTab(int tabIndex) {
        Set<String> windows = driver.getWindowHandles();
        ArrayList<String> l = new ArrayList<>(windows);
        driver.switchTo().window(l.get(tabIndex));
    }

    public String getLastWindowHandle() {
        Set<String> windowHandles = driver.getWindowHandles();
        return windowHandles.toArray()[windowHandles.size() - 1].toString();
    }

    public void switchToLastWindow() {
        String windowHandle = getLastWindowHandle();
        driver.switchTo().window(windowHandle);
    }

    public void closeLastWindow() {
        switchToLastWindow();
        driver.close();
    }

    public void switchToWindow(String windowTitle) {
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        while (iterator.hasNext()) {
            String windowHandle = iterator.next();
            driver.switchTo().window(windowHandle);
            String currentTitle = driver.getTitle();
            if (currentTitle.contains(windowTitle)) {
                return;
            }
        }
        throw new RuntimeException("Window with the title containing '" + windowTitle + "' was not found.");
    }

    public void switchToWindowByURL(String url) {
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        while (iterator.hasNext()) {
            String windowHandle = iterator.next();
            driver.switchTo().window(windowHandle);
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains(url)) {
                return;
            }
        }
        throw new RuntimeException("Window with the URL containing '" + url + "' was not found.");
    }

    public void delayFor(int milliseconds) {
        sleep(milliseconds);
    }

    protected void highlight(WebElement element) {
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 2px solid red;");
            sleep(200);
    }

    public void click(WebElement element) {
        waitForElementDisplayed(element, 20);
        highlight(element);
        element.click();
    }

    public void typeText(WebElement element, String text) {
        click(element);
        element.clear();
        element.sendKeys(text);
    }

    public void instagramTypeText(WebElement e, String text) {
        e.sendKeys(text);
    }


    public void verifyPageTitle(String expectedTitle) {
        waitForTitle(expectedTitle);
        String title = driver.getTitle();
        Assert.assertEquals(title, expectedTitle);
    }

    public void verifyPageText(WebElement element, String expectedText) {
        String actualText = element.getText().trim();
        assertThat(actualText, containsString(expectedText));
    }

    public void verifyElementDisplayed(WebElement element) {
        waitForElementDisplayed(element, 10);
        Assert.assertTrue(element.isDisplayed(), "Element is displaying");
    }

    public static void explicitWaitClickable(WebElement element, int time) {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getInstance().getDriver(),Duration.ofSeconds(time));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void explicitWaitVisibility(WebElement element, int time) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void explicitWaitInvisibility(WebElement element, int time) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForLoaderToDisappear() {
        FluentWait<WebDriver> waiter = fluentWait(20);
        waiter.until(ExpectedConditions.attributeContains(By.tagName("body"), "style", "opacity: 1"));
    }

    public WebElement waitForElementDisplayed(WebElement element, int timeToWaitInSec) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeToWaitInSec))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class)
                .ignoring((StaleElementReferenceException.class));

        WebElement waitTime = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                if (element != null && element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        });
        return waitTime;
    }

    public WebElement waitForElementEnabled(WebElement element, int timeToWaitInSec) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeToWaitInSec))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class);

        WebElement waitTime = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                if (element != null && element.isEnabled()) {
                    return element;
                }
                return null;
            }
        });
        return waitTime;
    }

    public void waitForPageLoad() {

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                logBuilder.info("Current Window State       : "
                        + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
    }

    public void WaitForModal()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean waitUntil = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//Div")));
    }

    public void explicitWaitForWindows(int numberOfWindows) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
    }

    public static boolean explicitWaitVisible(WebDriver driver, WebElement element, int time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void isElementPresent (By by, int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
        List<WebElement> dynamicElement = driver.findElements(by);
        if(dynamicElement.size() != 0)
            System.out.println("Element present");
        else
            System.out.println("Element not present");
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    // --------------------------------Wait for loader--------------------------

    public static void waitForLoader(WebElement element) {
        try {
            for (int k = 0; k < 500; k++) {
                if (explicitWaitVisible(DriverFactory.getInstance().getDriver(), element, 5)) {
                    continue;
                } else {
                    sleep(1);
                    break;
                }
            }
        } catch (Exception e) {
            // todo: eat the exception?
        }
    }

    public static void waitForLoader() {

        if (explicitWaitVisible(DriverFactory.getInstance().getDriver(),
                DriverFactory.getInstance().getDriver().findElement(By.xpath("//*[@class='panel-loading']")), 5)) {
            System.out.println("search executed");
        }
    }

    public static void sleep(int ms) {

       try {
           Thread.sleep(ms);
       } catch (InterruptedException ex) {

       }
    }

    public void scrollUp() {
        js.executeScript("window.scrollBy(0,-500)", "");
    }

    public void scrollDown() {
        js.executeScript("window.scrollBy(0,250)", "");

    }

    public void scrollDownTo() {
        js.executeScript("window.scrollBy(0,700)", "");
    }

    public void navigateAndRefresh(String url) {
        driver.navigate().to(url);
        driver.navigate().refresh();
    }

    public void reloadPage() {
        driver.navigate().refresh();
    }

    public void navigateBack() {

        driver.navigate().back();
//        int tries = 5;
//        try {
//            for (int i = 0; i < tries; i++) {
//                explicitWaitVisible(driver, driver.findElement(by), 5);
//            }
//        } catch (NoSuchElementException ex) {
//
//        }
    }

    public void scrollToBottomOfPage() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToTopOfPage() {
        js.executeScript("window.scrollBy(0,-250)", "");
    }

    public void scrollToElement(WebElement element) {
        waitForElementDisplayed(element, 30);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToEndUsingAction() {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
   }

    public static void log(String log) {
        System.out.println(log);
    }

    public void switchToWindow(int noOfWindows) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindows));
            for (String windowHandle : driver.getWindowHandles()) {
                win.add(windowHandle);
                driver.switchTo().window(windowHandle);
            }
        } catch (Exception e) {
            System.out.println("\n No window is displayed!");
        }
    }

    public void jsClick(WebElement element) {
        waitForElementDisplayed(element, 30);
        waitForElementEnabled(element, 30);
        waitForVisibilityOfElement(element);
        js.executeScript("arguments[0].click();", element);
    }

    public void jsTypeText(WebElement element, String inputText) {
        waitForElementDisplayed(element, 10);
        js.executeScript("arguments[0].value='" + inputText + "'", element);
    }
//	-----------------------Drop down functions-------------------------

    public void selectByVisibleTextFromDD(WebElement element, String value) {
        waitForElementDisplayed(element, 30);
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }

    public void selectByValueFromDD(WebElement element, String value) {
        waitForElementDisplayed(element, 30);
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public void selectByIndexFromDD(WebElement element, int index) {
        waitForElementDisplayed(element, 30);
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public String getSelectedOption(WebElement element) {
        waitForElementDisplayed(element, 30);
        Select select = new Select(element);
        WebElement option = select.getFirstSelectedOption();
        return option.getText().trim();
    }

    public static String generateRandomInt(int maxValue) {
        String randomInt = "";
        for (int i = 0; i <= 1000; i++) {
            int x = random.nextInt(maxValue);
            randomInt = Integer.toString(x);

//			To make sure the total number of digits in the random value generated is as expected.
//			Provide the max value as 1000 if we need a 3 digit random value

            String input = Integer.toString(maxValue);
            int inputLen = input.length();
            int outputLen = randomInt.length();
            int expectedLen = inputLen - 1;

            if (outputLen == expectedLen) {
                break;
            }
        }
        return randomInt;
    }

    public String generateRandomString() {
        String strNumbers = "abcdefghijklmnopqrstuvwxyzacvbe";
        StringBuilder strRandomNumber = new StringBuilder(9);
        strRandomNumber.append(strNumbers.charAt(random.nextInt(strNumbers.length())));
        String s1 = strRandomNumber.toString().toUpperCase();
        for (int i = 1; i < 4; i++) {
            strRandomNumber.append(strNumbers.charAt(random.nextInt(strNumbers.length())));
        }
        return s1 + strRandomNumber.toString();
    }

    @Deprecated
    public static void enterText(String text) {
        System.out.println("entering text");
        for (int i = 0; i < text.length(); i++) {
            char a = text.charAt(i);
            robot.keyPress(a);
            robot.keyRelease(a);
        }
    }

    public static void tabPress() {
        robot.keyPress(KeyEvent.VK_TAB);
    }

    public static void tabRelease() {
        robot.keyRelease(KeyEvent.VK_TAB);
    }

    public static void enterPress() {
        robot.keyPress(KeyEvent.VK_ENTER);
    }

    public static void enterRelease() {
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void waitForTitle(String title) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.titleContains(title));
    }

    public FluentWait<WebDriver> fluentWait(int duration) {
        return new FluentWait<WebDriver>(driver) // <3>
                .withTimeout(Duration.ofSeconds(duration)).pollingEvery(Duration.ofMillis(50))
                .ignoreAll(new ArrayList<Class<? extends Throwable>>() {
                    {
                        add(StaleElementReferenceException.class);
                        add(NoSuchElementException.class);
                    }
                }).withMessage("Selenium TimeoutException");
    }

    public void waitForVisibilityOfElement(WebElement element) {
        FluentWait<WebDriver> wait = fluentWait(30);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForStaleElement(WebElement element) {
        FluentWait<WebDriver> wait = fluentWait(30);
        wait.until(ExpectedConditions.stalenessOf(element));
    }

    public Boolean isCurrentlyDaylightSavingsTime(String region) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(region));
        ZoneId zId = now.getZone();
        ZoneRules zoneRules = zId.getRules();
        return zoneRules.isDaylightSavings(now.toInstant());
    }

    public void openNewTabWithUrl(int tabIndex, String url) {
        js.executeScript("window.open()");
        switchTab(tabIndex);
        driver.get(url);
    }

    public Date addMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }

    public String selectByVisibleTextFromDDWithReturnValues(WebElement element, String value) {
        selectByVisibleTextFromDD(element, value);
        return getSelectedOption(element);
    }

    public String getTextFromElement(WebElement element) {
        jsClick(element);
        return element.getAttribute("innerText");
    }

    public String sendAndGetTextFromElement(WebElement element, String value) {
        jsClick(element);
        element.clear();
        element.sendKeys(value);
        sleep(2);
        return element.getAttribute("value");
    }

    public int extractNumberFromString(String s) {
        return Integer.parseInt(s.replaceAll("[^0-9]", ""));
    }

    public String selectOptionAndGetTextFromDD(WebElement element, WebElement optionElement) {
        sleep(2);
        jsClick(element);
        sleep(2);
        jsClick(optionElement);
        return element.getAttribute("value");
    }

    public String generateName(WebElement element) {
        String name = faker.name().fullName();
        element.clear();
        element.sendKeys(name);
        return element.getAttribute("value");
    }

    public String generateNumber(WebElement element, int length) {
        String number = faker.number().digits(length);
        element.click();
        element.clear();
        element.sendKeys(number);
        return number;
    }

    public void verifyBrokenLinks() throws IOException {
        List<WebElement> linkList = driver.findElements(By.tagName("a"));
        linkList.addAll(driver.findElements(By.tagName("img")));
        List<WebElement> activeList = new ArrayList<>();

        for (int i = 0; i < linkList.size(); i++) {
            logBuilder.info(linkList.get(i).getAttribute("href"));
            if (linkList.get(i).getAttribute("href") != null && (!linkList.get(i).getAttribute("href").contains("javascript"))) {
                activeList.add(linkList.get(i));
            }

            for (int j = 0; j < activeList.size(); j++) {
                HttpURLConnection connection = (HttpURLConnection) new URL(activeList.get(j).getAttribute("href")).openConnection();
                connection.connect();
                String response = connection.getResponseMessage();
                connection.disconnect();
                logBuilder.info(activeList.get(j).getAttribute("href") + "-->" + response);
            }
        }
    }


/*    public void clickInTable(WebElement e, String colName, String value, List<WebElement> table) {
        List<String> list = getListDetailsFromMapTable(e, colName);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toLowerCase().equalsIgnoreCase(value.toLowerCase())) {
                table.get(i).click();
                logBuilder.info("Clicked " + value.toUpperCase() + "in row " + i);
                break;
            } else {
                logBuilder.info(value.toUpperCase() + " can't be found in row " + i);
            }
        }

    }

    public List<String> getListDetailsFromMapTable(List<WebElement> e, String colName) {
        HashMap<String, List<String>> tableMap;
        List<String> tableData = collectDataFromTable(e);
        tableMap = convertTableDataIntoMapWithHeaderAsKey(tableData);
        List<String> list = new ArrayList<>();
        list.addAll(tableMap.get(colName.toUpperCase()));
        logBuilder.info("List of " + colName + " :: " + list);

        return list;
    }

    public HashMap<String, List<String>> convertTableDataIntoMapWithHeaderAsKey(List<String> dataWithHeader) {
        HashMap<String, List<String>> tableMap = new HashMap<>();
        String[] headerArray = dataWithHeader.get(0).split(";");

        for (int i = 0; i < headerArray.length; i++) {
            List<String> columnList = new ArrayList<>();
            for (int j = 1; j < dataWithHeader.size(); j++) {
                String[] rowArray = dataWithHeader.get(j).split(";");
                columnList.add("@".equalsIgnoreCase(rowArray[i]) ? " " : rowArray[i]);
            }
            tableMap.put(headerArray[i], columnList);
            columnList = null;
        }
        return tableMap;
    }

    public List<String> collectDataFromTable(List<WebElement> dataTable) {
        List<String> data = new ArrayList<>();
        collectDataFromDataTable(dataTable, data, true);
        return data;
    }*/

/*    public void collectDataFromDataTable(List<WebElement> v dataTable, List<String> dataWithHeader, boolean addHeader) {
        boolean firstColFlag = false;
        boolean nextFlag = false;

        if (addHeader) {
            List<WebElement> dataTableHeader = dataTable.findElements(By.cssSelector("div[role='heading']"));
            StringBuilder headerBuilder = new StringBuilder();
            if (dataTableHeader.size() > 0) {
                for (WebElement header : dataTableHeader) {
                    String headerValue = header.getAttribute("role");
                    if (StringUtils.isNotBlank(headerValue)) {
                        String actualValue = header.getText();
                        headerBuilder.append(actualValue + ";");
                    } else {
                        firstColFlag = true;
                    }

                }
                headerBuilder.deleteCharAt(headerBuilder.length() - 1);
                dataWithHeader.add(headerBuilder.toString());
            }
        }
        boolean text = false;
        do {
            List<WebElement> webTableDataBody = dataTable.findElements(By.cssSelector("div[class='rt-tr-group']")); //
            if (webTableDataBody.size() > 0) {
                int size = webTableDataBody.size();
                logBuilder.info("Size is: " + size);
                for (int j = 0; j < webTableDataBody.size(); j++) {
                    List<WebElement> columnData = webTableDataBody.get(j).findElements(By.cssSelector("rt-td"));
                    StringBuilder columnDataBuilder = new StringBuilder();
                    for (int i = 0; i < columnData.size(); i++) {
                        String columnsActualElement;
                        try {
                            if (i == 0 && firstColFlag) {
                                continue;
                            }
                            columnsActualElement = columnData.get(i).getText();
                            columnDataBuilder.append(("".equalsIgnoreCase(columnsActualElement) ? "@" : columnsActualElement) + ";");
                            text = true;
                        } catch (NoSuchElementException exception) {
                            logBuilder.info("The first column doesn't have a value.");
                        }
                    }

                    {

                    }
                }
            } else {
                logBuilder.info("No record displayed.");
            }
            if (!(isElementEnabled(RFIPage.RFIListNextButton))) {
                nextFlag = true;
            }
            if (!nextFlag) {
                if (text && !(isElementEnabled(RFIPage.RFIListNextButton))) {
                    click(RFIPage.RFIListNextButton);
                    text = false;
                }
            }
        }
        while (!text);
    }*/
}
