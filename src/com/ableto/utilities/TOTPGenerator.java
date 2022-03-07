package com.ableto.utilities;

import org.jboss.aerogear.security.otp.Totp;

public class TOTPGenerator {
    /**
     * Method is used to get the TOTP based on the security token
     * @return
     */
    public static String getTwoFactorCode() {

        String secret = ConfigManager.getInstance().getString("totpSecret");
        
        //Replace with your security key copied from step 12
        //Totp totp = new Totp(secret); // 2FA secret key for qa.test@ableto.com
        Totp totp = new Totp("bzi7 ldse 36mc h64q zde6 snhd p6oa sb6r"
        ); // 2FA secret key for qa.test@ableto.com

        String twoFactorCode = totp.now(); //Generated 2FA code here
        return twoFactorCode;
    }
}
