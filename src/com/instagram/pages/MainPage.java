package com.instagram.pages;

import com.instagram.base.PageBase;
import com.instagram.models.Influencer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertEquals;

public class MainPage extends PageBase {

    @FindBy(xpath = "//span[text()='Search']")
    public WebElement searchFirefox;

    @FindBy(xpath = "//input[@placeholder='Search']")
    public WebElement searchInput;

    @FindBy(css = "section div h2")
    public WebElement influencerName;

    @FindBy(css = "section div h1")
    public WebElement influencerNameAlternative;

    @FindBy(css = "header section span span button")
    public WebElement follow;

    public static By message = By.xpath("//button[contains(.,'Message')]");

    @FindBy(xpath = "//textarea[@placeholder='Message...']")
    public WebElement messageInput;

    @FindBy(xpath = "//button[text()='Send']")
    public WebElement sendBtn;

    public MainPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    LinkedList<String> influencers = new LinkedList<>();
    List<String> disqualified = new ArrayList<>();

    public void searchForInfluencers() throws IOException {

    click(searchFirefox);
    typeText(searchInput, "Orlando, Florida");

    By result = By.cssSelector("ul li");
		List<WebElement> resultList = driver.findElements(result);
		resultList.forEach (r -> {
			if(r.getText().equalsIgnoreCase("Orlando, Florida")) {
				click(r);
			}
		});
    By orlandoFlorida = By.xpath("//a[@href='/explore/locations/212971112/orlando-florida/']");
    // By orlandoFlorida = By.xpath("//main/div/following-sibling::div/a[@role='link']");
    jsClick(driver.findElement(orlandoFlorida));
    delayFor(5000);
    iterateOverInfluencers();
}

String messageToInfluencer = "test";


public List<WebElement> getOrlandoResults() {
    By orlandoResults = By.xpath("//article/following-sibling::div//a");
    return driver.findElements(orlandoResults);
}

public void iterateOverInfluencers() throws IOException {


    By profile = By.xpath("//article/div/following-sibling::div//a");
    By followers = By.xpath("//a[contains(., 'followers')]");
    delayFor(8000);

    List<WebElement> orlandoList = getOrlandoResults();
    System.out.println(orlandoList.size());

    List<String> uniqueInfluencersInOrlandoList = new ArrayList<>();

    delayFor(1000);
    orlandoList.forEach(src -> {
        String s = src.getDomAttribute("href");
        if (!uniqueInfluencersInOrlandoList.contains(s)) {
            uniqueInfluencersInOrlandoList.add(s);
        }
    });
    System.out.println("UniqueList size : " + uniqueInfluencersInOrlandoList.size());
    for (int j = 0; j < uniqueInfluencersInOrlandoList.size(); j++) {
        delayFor(8000);
        List<WebElement> list = getOrlandoResults();
        String domAttribute = list.get(j).getDomAttribute("href");
        System.out.println(domAttribute);
        jsClick(driver.findElement(By.xpath("//a[contains(@href,'" + uniqueInfluencersInOrlandoList.get(j) + "')]")));

        click(driver.findElement(profile));

        delayFor(500);
        Influencer inf = new Influencer();
        String influencer;
        try {
            influencer = influencerName.getText();
        } catch (NoSuchElementException ex) {
            influencer = influencerNameAlternative.getText();
        }
        inf.setInfluencerName(influencer);

        try {
            isElementPresent(By.xpath("//span[@aria-label='Following']"), 2);
            Integer numberOfFollowers;
            String s = driver.findElement(followers).getText();
            String s2 = s.replaceAll("k", "000").replaceAll("\\.", "").replaceAll("followers", "").replaceAll("follower", "").replaceAll(",", "").replaceAll("m", "000000").replaceAll("\\s", "");

            numberOfFollowers = Integer.parseInt(s2);
            if (influencers.size() > 10) {
                Assert.assertTrue(true, "more than 10 influencers were found");
                break;
            }

            String messageToDisqualified = "Hi " + influencer + "! Do you like Pancakes and like to do something fun? My wife, Amira, and I just started Ella's Mini Pancakes, the very first mini pancakes stand right here in Orlando! We are at the Central Florida Fair on Colonial Drive right now! If you don't have plans yet why don't you call your friends, swing by the Fair, enjoy our fresh mini pancakes, and support your local business by snapping a picture for Instagram? Either way, have an awesome evening! -David";

            String filePath = "C:\\Users\\Amira\\Documents\\";
            String fileName;

            if (numberOfFollowers < 5000) {

                disqualified.add(influencer);
                sendMessageToInfluencer(influencer, messageToDisqualified, false);
                fileName = "nonInfluencers.txt";
                writeFileToFileName(filePath, fileName, influencer);

            } else {
                String messageToInfluencer = "Hi " + influencer + "! Do you like Pancakes and like to do something fun? My wife, Amira, and I just started Ella's Mini Pancakes, the very first mini pancakes stand right here in Orlando! We are at the Central Florida Fair on Colonial Drive right now! If you don't have plans yet why don't you call your friends, swing by the Fair, get one free order of our mini pancakes, and support your local business by snapping a picture for Instagram? Either way, have an awesome evening! -David";
                fileName = "influencersFile.txt";

                if (!verifyStringInFileExists(filePath + fileName, influencer)) {
                    sendMessageToInfluencer(influencer, messageToInfluencer, true);
                    writeFileToFileName(filePath, fileName, influencer);
                }
            }
            System.out.println(influencers.size());
            influencers.forEach(i -> System.out.println("influencer: " + i));
            System.out.println(disqualified.size());
            disqualified.forEach(i -> System.out.println("disqualified: " + i));

            navigateBack();
            navigateBack();
                    ArrayList<WebElement> buttons = (ArrayList<WebElement>)
                            driver.findElements(By.cssSelector("button"));
                    for (int i = 0; i < buttons.size() ; i++) {
                        Random random = new Random();
                        int integer = random.nextInt();
                        buttons.get(integer).click();
                    }
                    delayFor(7000);
                    navigateBack();
        } catch (Exception ex) {
            navigateBack();
            navigateBack();
        }
    }
}

public void sendMessageToInfluencer (String accountName, String messageToAccount, boolean send) {
    influencers.add(accountName);
    try {
        isElementPresent(message, 2);
        click(driver.findElement(message));
    } catch (Exception ex) {
        click(follow);
        click(driver.findElement(message));
    }
    typeText(messageInput, messageToAccount);
    delayFor(3000);
    if(send) {
        click(sendBtn);
        delayFor(7000);
    }
    navigateBack();
}

public void writeFileToFileName (String filePath, String fileName, String content) throws IOException {

    Path path = Paths.get(filePath + fileName);
    File f = new File(filePath + fileName);
    if(!f.isFile()) {

        byte[] strToBytes = content.getBytes();

        Files.write(path, strToBytes);

        String read = Files.readAllLines(path).get(0);

        assertEquals(content, read);
    } else {
        appendToFile(filePath, fileName, content);                }
}

public void appendToFile (String filePath, String fileName, String content) throws IOException {

    content = content + "\r\n";
    Files.write(
            Paths.get(filePath + fileName),
            content.getBytes(),
            StandardOpenOption.APPEND);
}

public boolean verifyStringInFileExists (String path, String s) throws IOException {
    return Files.lines(Paths.get(path)).anyMatch(l -> l.contains(s));
}
        }

