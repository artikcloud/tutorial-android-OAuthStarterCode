* Edit ./app/src/main/java/cloud/artik/akcsampleauth/FullscreenActivity.java :
    - Set your CLIENT_ID, and REDIRECT_URI.
    - WARNING : use a unique single REDIRECT_URI for your application.
    - When the user is authenticated, decide what to do (instead of
      calling showAuthInfo() ), e.g. start a new activity
* Edit ./app/src/main/AndroidManifest.xml :
    - Make sure the last intent filter respects your REDIRECT_URI:
        - e.g. if your REDIRECT_URI is "oauth2://redirect_url" then
        the intent-filter will contain :
            - <data android:scheme="oauth2" />
            - <data android:host="redirect_url" />
