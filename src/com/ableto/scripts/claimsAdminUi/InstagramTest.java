package com.ableto.scripts.claimsAdminUi;

import com.ableto.base.BaseTest;
import com.ableto.utilities.ExtentReporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;



@Listeners(ExtentReporter.class)
public class InstagramTest extends BaseTest {

    @Test()
    public void login() throws IOException {
        loginPage.loginToInstagram();
    }


}
