# Android OAuth Starter Code

Quick start code for making OAuth API call to get a user token from ARTIK Cloud

## Prerequisites
* Android Studio

## Setup / Installation:

### Set up at ARTIK Cloud

Follow [these instructions](https://developer.artik.cloud/documentation/tutorials/your-first-application.html#create-an-application) to create an application using the Developer Dashboard. For this tutorial, select the following:

- Set "Redirect URL" for your application to `http://example.com/redirect_url`.
- Choose "Client Credentials, auth code, implicit".

[Make a note of your client ID.](https://developer.artik.cloud/documentation/tools/web-tools.html#how-to-find-your-application-id) This is your application ID, which you will need in the next step.

### Set up your Android project

- Get the sample application Android project at <a href="https://github.com/artikcloud/tutorial-android-your-first-app" target="_blank">**GitHub**</a>
- Change `CLIENT_ID` to your client ID (application ID) at the following line in `MainActivity.java`:

~~~java
private static final String CLIENT_ID = "<YOUR CLIENT ID>";
~~~

- Make sure `REDIRECT_URI` at the following line in `MainActivity.java` is consistent with "Redirect URL" for your application at the Developer Dashboard:

~~~java
public static final String REDIRECT_URI = "http://example.com/redirect_url";
~~~

- Make sure the intent filter fields at the following lines in `AndroidManifest.xml` respect "Redirect URL" for your application at the Developer Dashboard:

~~~xml
<data android:scheme="http" />
<data android:host="example.com/redirect_url" />
~~~

- Build the Android project of the sample application.

## Usage

- Run the Android app. Sign in or sign up on the following screen:

![GitHub Logo](./img/screenshot-signin-signup.png)

- Receive the access token after login succeeds:

![GitHub Logo](./img/screenshot-receive-accesstoken.png)

## More examples

Peek into Andriod applications in [Tutorials](https://developer.artik.cloud/documentation/tutorials/) and [Samples](https://developer.artik.cloud/documentation/samples/) for more examples.

More about ARTIK Cloud
---------------

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-reference/

Peek into advanced sample applications at https://developer.artik.cloud/documentation/samples/

To create and manage your services and devices on ARTIK Cloud, visit the Developer Dashboard at https://developer.artik.cloud

License and Copyright
---------------------

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2016 Samsung Electronics Co., Ltd.
