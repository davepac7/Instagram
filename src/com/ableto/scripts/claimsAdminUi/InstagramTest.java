package com.ableto.scripts.claimsAdminUi;

import com.ableto.base.BaseTest;
import com.ableto.models.Claim;
import com.ableto.utilities.ExtentReporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static com.ableto.utilities.Constants.HELD;
import static com.ableto.utilities.Constants.SUPPRESSED;


@Listeners(ExtentReporter.class)
public class InstagramTest extends BaseTest {

    @Test()
    public void login() throws IOException {
        loginPage.loginToInstagram();
    }


}
