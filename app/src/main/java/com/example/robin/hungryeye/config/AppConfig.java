package com.example.robin.hungryeye.config;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by robin on 12/2/2015.
 */
public class AppConfig {
    public static final String URL_VERIFY_PAYMENT = "http://hungryeye.gear.host" ;
    //url name for login
    public static final String LOGIN_URL ="http://hungryeye.gear.host";
    //url name for register
    public static final String REGISTER_URL="http://hungryeye.gear.host";
    //url for getting menu list
    public static final String CONTENT_URL="http://hungryeye.gear.host";
    //url for getting image list
    public static final String IMAGES_URL="http://hungryeye.gear.host";

    // PayPal app configuration
    public static final String PAYPAL_CLIENT_ID = "AR7m9KosW8TH5jelsKm67AqludLKOQNU3TyfEUiJWvgIIz0zAahqz5NFixbfYCprMRM_egdnNTL2yUn_";
    public static final String PAYPAL_CLIENT_SECRET = "";


    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    //public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "GBP";


    // PayPal configuration
    public static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(AppConfig.PAYPAL_ENVIRONMENT).clientId(
                    AppConfig.PAYPAL_CLIENT_ID);
}
