package cloud.artik.example.oauth;

public class Config {

    /**
     * IMPORTANT:  This sample application, for demonstration purposes embeds the client
     * secret below to perform a 'code' exchange.
     *
     * To follow IETF best practice for Oauth2.0, native client should implement
     * Authorization Code method + PKCE
     *
     * https://tools.ietf.org/html/rfc7636#section-4.4
     *
     * Stay tuned for improving this sample to use AC+PKCE
     */
    // Replace the following entries with your own settings:
    public static final String CLIENT_ID = "YOUR_CLIENT_ID";
    public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";

    // MUST be consistent with "AUTH REDIRECT URL" of your application set up at the developer.artik.cloud
    public static final String REDIRECT_URI = "cloud.artik.example.oauth://oauth2callback";

    // You should not change the following
    public static final String ARTIKCLOUD_AUTHORIZE_URI = "https://accounts.artik.cloud/signin";
    public static final String ARTIKCLOUD_TOKEN_URI = "https://accounts.artik.cloud/token";
}
