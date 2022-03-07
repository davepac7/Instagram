package com.ableto.pages.claimsAdmin;

import com.ableto.base.PageBase;

import static com.ableto.utilities.Constants.*;

import com.ableto.models.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.page.model.VisualViewport;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedList;
import java.util.List;

public class ClaimsAdminPage extends PageBase {

    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement signInBtn;

    @FindBy(css = "div[data-testid='failed']")
    private WebElement failed;

    @FindBy(css = "div[data-testid='claim-errors']")
    private WebElement claimErrors;

    @FindBy(css = "div[data-testid='rejected']")
    private WebElement rejected;

    @FindBy(css = "div[data-testid='held']")
    private WebElement held;

    @FindBy(css = "div[data-testid='suppressed']")
    private WebElement suppressed;

    @FindBy(css = "div[data-testid='outbound']")
    private WebElement outbound;

    @FindBy(css = "div[data-testid='paid']")
    private WebElement paid;

    @FindBy(css = "th[data-testid='member-id']")
    private WebElement memberId;

    @FindBy(css = "th[data-testid='claim-id']")
    private WebElement claimId;

    @FindBy(xpath = "//button[text()='Yes, suppress']")
    private WebElement yesSuppress;

    @FindBy(xpath = "//button[text()='Yes, unsuppress']")
    private WebElement yesUnsuppress;

    @FindBy(xpath = "//button[text()='Suppress Selected Claims']")
    private WebElement suppressClaims;

    @FindBy(xpath = "//button[text()='Unsuppress Selected Claims']")
    private WebElement unSuppressClaims;


    public ClaimsAdminPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    public void login() {
        boolean loggedIn= false;
        try {
            explicitWaitVisibility(signInBtn, 1);
        } catch (TimeoutException ex) {
            loggedIn = true;
        }
        if(!loggedIn) {
            click(signInBtn);
        }
    }

    public void verifyClaimsInTable(String tab, LinkedList<Claim> claims) {

        WebElement e;
        switch (tab) {
            case FAILED:
                e = failed;
                break;
            case CLAIM_ERRORS:
                e = claimErrors;
                break;
            case REJECTED:
                e = rejected;
                break;
            case HELD:
                e = held;
                break;
            case SUPPRESSED:
                e = suppressed;
                break;
            case OUTBOUND:
                e = outbound;
                break;
            case PAID:
                e = paid;
                break;
            default:
                throw new IllegalStateException("invalid tab: " + tab);
        }

        click(e);

        claims.forEach(claim -> {
            verifyElementDisplayed(driver.findElement(By.cssSelector("td[data-testid='" + claim.getClaimId() + "-" + PROTOCOL_CODE + "'] span")));
        });

    }

    public void suppressClaim(LinkedList<Claim> claims) {

        Claim claim = claims.get(0);

        By hamburgerBtn = By.cssSelector("tr[data-testid='claim-" + claim.getClaimId() + "'] button");

        By suppressBtn = By.cssSelector("button[data-testid='suppress-item']");


        click(held);
        click(driver.findElement(hamburgerBtn));
        click(driver.findElement(suppressBtn));
        click(yesSuppress);
        verifyClaimsInTable(SUPPRESSED, claims);
    }

    public void unSuppressClaim(LinkedList<Claim> claims) {

        Claim claim = claims.get(0);

        By hamburgerBtn = By.cssSelector("tr[data-testid='claim-" + claim.getClaimId() + "'] button");

        By unSuppressBtn = By.cssSelector("button[data-testid='unsuppress-item']");

        click(suppressed);
        click(driver.findElement(hamburgerBtn));
        click(driver.findElement(unSuppressBtn));
        click(yesUnsuppress);
        verifyClaimsInTable(HELD, claims);
    }

    public void batchSuppressClaims(LinkedList<Claim> claims) {

        click(held);

        claims.forEach(claim -> {
            By checkbox = By.cssSelector("tr[data-testid='claim-" + claim.getClaimId() + "'] input");
            click(driver.findElement(checkbox));
        });

        click(suppressClaims);
        verifyClaimsInTable(SUPPRESSED, claims);
    }

    public void batchUnsuppressClaims(LinkedList<Claim> claims) {

        click(suppressed);
        driver.navigate().refresh();
        claims.forEach(claim -> {
            By checkbox = By.cssSelector("tr[data-testid='claim-" + claim.getClaimId() + "'] input");
            click(driver.findElement(checkbox));
        });

        click(unSuppressClaims);
        verifyClaimsInTable(HELD, claims);
    }
}
