package com.example.sociallib.app.extendedmodel;


import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class GoogleSocialObject extends SocialObject {

    private String mClientId;
    private String mRedirectUri;
    private String mToken;
    private static final String FIRST_NAME = "givenName";
    private static final String LAST_NAME = "familyName";

    /**
     * @param pSocialCallback Callback object. SocialCallback interface should be implemented.
     * @param pClientId    Google application ID
     * @param pRedirectUri Redirect URL
     */

    public GoogleSocialObject(SocialCallback pSocialCallback, String pClientId, String pRedirectUri) {
        mSocialCallback = pSocialCallback;
        mClientId = pClientId;
        mRedirectUri = pRedirectUri;
    }

    @Override
    public Boolean isParseResponseSuccess(String response) {

        if (response.contains(ACCESS_TOKEN) && (!response.contains(ERROR_CONST))) {
            String [] result = response.split("[=&]+");
            mToken = result[1];
            Bundle googleBundle = new Bundle();
            googleBundle.putString(ACCESS_TOKEN, mToken);
            mSocialCallback.isSucceed(googleBundle);
            return true;
        } else if (response.contains(ERROR_CONST)) {
            Bundle errorBundle = new Bundle();
            errorBundle.putString(ERROR_CONST, response);
            mSocialCallback.isFailed(errorBundle);
            return false;
        }
        return false;
    }

    @Override
    public String getUrl() {
        return "https://accounts.google.com/o/oauth2/auth?client_id=" + mClientId + "&response_type=token&scope=email&redirect_uri=" + mRedirectUri + "&login_hint=jsmith@example.com";
    }

    @Override
    public SocialUser getUser(String pToken) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://www.googleapis.com/plus/v1/people/me?access_token=" + mToken);

                String name = null;
                String surname = null;
                String email = null;

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    JSONObject resultJson = new JSONObject(result);
                    Log.d("googleplus", resultJson.toString());
                    name = resultJson.getJSONObject("name").getString(FIRST_NAME);
                    surname = resultJson.getJSONObject("name").getString(LAST_NAME);
                    email = resultJson.getJSONArray("emails").getJSONObject(0).getString("value");

                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                socialUser = new SocialUser(name, surname, email);
                Bundle googleBundle = new Bundle();
                googleBundle.putParcelable(USER_BUNDLE, new SocialUser(name, surname, email));
                mSocialCallback.isSucceed(googleBundle);
            }
        }).start();

        return null;
    }


}
