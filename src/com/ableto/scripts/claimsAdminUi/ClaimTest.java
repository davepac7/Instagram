package com.ableto.scripts.claimsAdminUi;

import com.ableto.base.BaseTest;
import com.ableto.models.Claim;
import com.ableto.utilities.ExtentReporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static com.ableto.utilities.Constants.HELD;
import static com.ableto.utilities.Constants.SUPPRESSED;


@Listeners(ExtentReporter.class)
public class ClaimTest extends BaseTest {

    @Test(groups = {"rcm"})
    public void verifyClaims() {

        Claim claim = new Claim();
        claim.setClaimId("cd2dcc1a-1403-4187-a13a-06b3a50bb13d");
        claim.setProtocolName("next_step_w3_dep_v5");

        LinkedList<Claim> claims = new LinkedList<>(Arrays.asList(claim));

        extent.HeaderChildNode(":::::::::::::  Verify Claim  ::::::::::::::::");
        logBuilder.startTestCase(this.getClass().getName());
        claimsAdminPage.login();
        claimsAdminPage.verifyClaimsInTable(HELD, claims);
    }

    @Test(groups = {"rcm"})
    public void suppressClaim () {

        Claim claim2 = new Claim();
        claim2.setClaimId("b026a7d1-c533-4d3b-8dcf-260762c20b0c");
        claim2.setProtocolName("introduction_to_joyable");

        LinkedList<Claim> claims = new LinkedList<>(Arrays.asList(claim2));

        extent.HeaderChildNode(":::::::::::::  Suppress Claim  ::::::::::::::::");
        logBuilder.startTestCase(this.getClass().getName());
        claimsAdminPage.login();
        claimsAdminPage.suppressClaim(claims);
        claimsAdminPage.verifyClaimsInTable(SUPPRESSED, claims);
        claimsAdminPage.unSuppressClaim(claims);
        claimsAdminPage.verifyClaimsInTable(HELD, claims);
    }

    @Test(groups = {"rcm"})
    public void batchSuppressClaims () {

        Claim claim3 = new Claim();
        claim3.setClaimId("ab0a9fc8-3647-4f33-a5ae-ad62acc63f0b");
        claim3.setProtocolName("next_step_w2_dep_v5");

        Claim claim4 = new Claim();
        claim4.setClaimId("23ec03fc-e1f5-4665-8e14-2bf20a892d1b");
        claim4.setProtocolName("dep_our_anxious_thoughts");

        Claim claim5 = new Claim();
        claim5.setClaimId("2863a0b7-347a-4dfd-9ab6-8b3a2ec14d84");
        claim5.setProtocolName("build_your_path");

        LinkedList<Claim> claims = new LinkedList<>(Arrays.asList(claim3, claim4, claim5));

        extent.HeaderChildNode(":::::::::::::  Batch Suppress Claims  ::::::::::::::::");
        logBuilder.startTestCase(this.getClass().getName());
        claimsAdminPage.login();
        claimsAdminPage.batchSuppressClaims(claims);
        claimsAdminPage.batchUnsuppressClaims(claims);
    }

}
