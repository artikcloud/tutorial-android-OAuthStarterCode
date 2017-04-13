package cloud.artik.example.oauth;

import android.app.Activity;
import android.net.Uri;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class AuthHelper extends Activity {

    public static final String INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE
            = "cloud.artik.example.oauth.ARTIKCLOUD_AUTHORIZATION_RESPONSE";
    public static final String USED_INTENT = "USED_INTENT";

    public static AuthorizationRequest createAuthorizationRequest() {

        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse(Config.ARTIKCLOUD_AUTHORIZE_URI),
                Uri.parse(Config.ARTIKCLOUD_TOKEN_URI),
                null
        );

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                Config.CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(Config.REDIRECT_URI)
        );

        return builder.build();

    }

}
