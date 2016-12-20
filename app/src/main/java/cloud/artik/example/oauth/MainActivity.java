package cloud.artik.example.oauth;

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

    // ARTIK Cloud application information
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

                // The login is successful or come back to login after finishing signup
            if (uri.startsWith(REDIRECT_URI)) {
                //login succeed
                if (uri.contains("access_token=")) {
                    // Example of uri: http://example.com/redirect_url#access_token=xxx&refresh_token=xxx&token_type=bearer&expires_in=1209600
                    // Extracts access token
                    String[] sArray = uri.split("access_token=");
                    String accessToken = sArray[1];
                    MainActivity.accessToken = accessToken.split("&")[0];

                    sArray = uri.split("refresh_token=");
                    String refreshToken = sArray[1];
                    MainActivity.refreshToken = refreshToken.split("&")[0];

                    sArray = uri.split("expires_in=");
                    String expiresIn = sArray[1];
                    MainActivity.expiresIn = expiresIn.split("&")[0];

                    // TODO: Start your new activity here instead of showing the results of successful OAuth flow.
                    // For example
                    // Intent myIntent = new Intent(this, cloud.artik.XXXActivity.class);
                    // this.startActivity(myIntent);
                    showAuthInfo();
                    return true;
                }
                else { // No access token available. Signup finishes and come back to the login page again
                    // Example of uri: http://example.com/redirect_url?origin=signup&status=login_request
                    //
                    eraseAuthentication();
                    return true;
                }
            }

                return super.shouldOverrideUrlLoading(view, uri);
            }
        });
        // Load the ARTIK Cloud Accounts Login webpage
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
