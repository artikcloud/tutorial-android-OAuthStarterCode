package cloud.artik.akcsampleauth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    // OAUTH2 implicit (mobile) authentication information
    public static String accessToken = "";
    public static String refreshToken = "";
    public static String expiresIn = "";

    // ARTIK Cloud application informations
    public static final String AKC_BASE_URL = "https://accounts.artik.cloud";
    public static final String CLIENT_ID = "<YOUR CLIENT ID>";// AKA application id
    public static final String REDIRECT_URI = "http://example.com/redirect_url";

    // Webview thanks to which the authentication information is extracted
    public static WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.loginWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            /**
             * Override URL Loading : to catch when the user has logged in and switch to the
             * next activity
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                // The login is successful

                // the redirect_uri could be also localhost://
                if (uri.startsWith(REDIRECT_URI)) {
                    if (uri.contains("access_token=")) {
                        // Extracts SAMI access token
                        String[] sArray = uri.split("access_token=");
                        String accessToken = sArray[1];
                        MainActivity.accessToken = accessToken.split("&")[0];

                        sArray = uri.split("refresh_token=");
                        String refreshToken = sArray[1];
                        MainActivity.refreshToken = refreshToken.split("&")[0];

                        sArray = uri.split("expires_in=");
                        String expiresIn = sArray[1];
                        MainActivity.expiresIn = expiresIn.split("&")[0];


                        // TODO: Please start your new activity here.
                        // The OAUTH2 implicit authentication information is available
                        // as static fields in the MainActivity class
                        showAuthInfo();

//                        Intent myIntent = new Intent(this, cloud.artik.XXXActivity.class);
//                        this.startActivity(myIntent);

                        return true;
                    } else {
                        // No access token available : display the main login page again
                        // TODO: Please undertake the action you prefer
                        eraseAuthentication();
                        return true;
                    }
                }


                return super.shouldOverrideUrlLoading(view, uri);
            }
        });
        // Load the Samsung SAMI Accounts Login webpage
        webView.loadUrl(getAuthorizationRequestUri());

    }


    // Helpers
    public static String getAuthorizationRequestUri() {
        return AKC_BASE_URL + "/authorize?client=mobile&response_type=token&client_id=" + CLIENT_ID;
    }

    public static void eraseAuthentication() {
        CookieManager cookieManager = CookieManager.getInstance();
        CookieManager.getInstance().removeAllCookie();
        webView.loadUrl(getAuthorizationRequestUri());
    }


    public void showAuthInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("accessToken = " + MainActivity.accessToken + "\n" + "refreshToken = " + MainActivity.refreshToken + "\n" + "expiresIn = " + MainActivity.expiresIn + "\n").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();

    }
}
