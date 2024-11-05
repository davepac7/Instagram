package com.instagram.scripts.Instagram;

import com.instagram.base.BaseTest;
import com.instagram.utilities.ExtentReporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;



@Listeners(ExtentReporter.class)
public class InstagramTest extends BaseTest {

    @Test(groups = {"instagram"})
    public void login() throws IOException {
        loginPage.loginToInstagram();
        mainPage.searchForInfluencers();
    }


}
