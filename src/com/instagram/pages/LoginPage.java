package com.instagram.pages;

import com.instagram.base.PageBase;
import com.instagram.models.Influencer;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static org.testng.Assert.assertEquals;


public class LoginPage extends PageBase {

    @FindBy(css = "input[aria-label='Phone number, username, or email']")
    public WebElement username;

    @FindBy(css = "input[aria-label='Password']")
    public WebElement password;

    @FindBy(xpath = "//div[text()='Log in']")
    public WebElement login;

    @FindBy(xpath = "//div[text()='Not now']")
    public WebElement notNow;

    @FindBy(xpath = "//button[text()='Cancel']")
    public WebElement cancel;

    @FindBy(xpath = "//a[@href='/explore/']")
    public WebElement search;


    public LoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }


    public void loginToInstagram() throws IOException {
        System.getenv("INSTAGRAM_PASSWORD");

        String pwordMisterekQa = System.getenv("INSTAGRAM_PASSWORD_MISTEREKQA");
        typeText(username, "ellasminipancakes");
        typeText(password, pwordMisterekQa);


        click(login);
        delayFor(3000);
        click(notNow);

        try {
            click(notNow);
        } catch (Exception ex) {
        }
    }
}