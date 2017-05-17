/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.artik.example.oauth;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import static cloud.artik.example.oauth.AuthHelper.INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE;
import static cloud.artik.example.oauth.AuthHelper.USED_INTENT;


public class LoginActivity extends AppCompatActivity {

    public static String accessToken = "";
    public static String refreshToken = "";
    public static String expiresAt = "";

    Button mButtonSignIn;

    static final String LOG_TAG = "LoginActivity";

    AuthorizationService mAuthorizationService;
    AuthStateDAL mAuthStateDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Entering onCreate ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthorizationService = new AuthorizationService(this);
        mButtonSignIn = (Button) findViewById(R.id.btn_login);
        mButtonSignIn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                doAuth();
            }

        });

        mAuthStateDAL = new AuthStateDAL(this);
    }

    // File OAuth call with Authorization Code method
    // https://developer.artik.cloud/documentation/getting-started/authentication.html#authorization-code-method
    private void doAuth() {
        AuthorizationRequest authorizationRequest = AuthHelper.createAuthorizationRequest();

        PendingIntent authorizationIntent = PendingIntent.getActivity(
                this,
                authorizationRequest.hashCode(),
                new Intent(INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE, null, this, LoginActivity.class),
                0);

        /* request sample with custom tabs */
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        mAuthorizationService.performAuthorizationRequest(authorizationRequest, authorizationIntent, customTabsIntent);

    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "Entering onStart ...");
        super.onStart();
        checkIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        checkIntent(intent);
    }

    private void checkIntent(@Nullable Intent intent) {

        Log.d(LOG_TAG, "Entering checkIntent ...");
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE:
                    Log.d(LOG_TAG, "checkIntent action = " + action
                            + " intent.hasExtra(USED_INTENT) = " + intent.hasExtra(USED_INTENT));
                    if (!intent.hasExtra(USED_INTENT)) {
                        handleAuthorizationResponse(intent);
                        intent.putExtra(USED_INTENT, true);
                    }
                    break;
                default:
                    Log.w(LOG_TAG, "checkIntent action = " + action);
                    // do nothing
            }
        } else {
            Log.w(LOG_TAG, "checkIntent intent is null!");
        }
    }

    private void handleAuthorizationResponse(@NonNull Intent intent) {

        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        Log.i(LOG_TAG, "Entering handleAuthorizationResponse with response from Intent = " + response.jsonSerialize().toString());

        if (response != null) {

            if (response.authorizationCode != null ) { // Authorization Code method: succeeded to get code

                final AuthState authState = new AuthState(response, error);
                Log.i(LOG_TAG, "Received code = " + response.authorizationCode + "\n make another call to get token ...");

                // File 2nd call in Authorization Code method to get the token
                mAuthorizationService.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            mAuthStateDAL.writeAuthState(authState); //store into persistent storage for use later
                            String text = String.format("Received token response [%s]", tokenResponse.jsonSerializeString());
                            Log.i(LOG_TAG, text);
                            accessToken = tokenResponse.accessToken;
                            expiresAt = tokenResponse.accessTokenExpirationTime.toString();
                            refreshToken = tokenResponse.refreshToken;
                            showAuthInfo();
                        } else {
                            Context context = getApplicationContext();
                            Log.w(LOG_TAG, "Token Exchange failed", exception);
                            CharSequence text = "Token Exchange failed";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
            } else { // come here w/o authorization code. For example, signup finish and user clicks "Back to login"
                Log.d(LOG_TAG, "additionalParameter = " + response.additionalParameters.toString());

                if (response.additionalParameters.get("status").equalsIgnoreCase("login_request")) {
                    // ARTIK Cloud instructs the app to display a sign-in form
                    doAuth();
                } else {
                    Log.d(LOG_TAG, response.jsonSerialize().toString());
                }
            }

        } else {
            Log.w(LOG_TAG, "Authorization Response is null ");
            Log.d(LOG_TAG, "Authorization Exception = " + error);
        }
    }

    public void showAuthInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("accessToken = " + accessToken + "\n" + "refreshToken = " + refreshToken + "\n" + "expiresAt = " + expiresAt + "\n").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }
}


