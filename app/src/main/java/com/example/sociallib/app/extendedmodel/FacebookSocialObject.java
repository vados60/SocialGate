package com.example.sociallib.app.extendedmodel;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FacebookSocialObject extends SocialObject {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private String mClientId;
    private String mRedirectUri;
    private SocialCallback mSocialCallback;
    private String mToken;
    private SocialUser socialUser;

    /**
     * @param pSocialCallback Callback object. SocialCallback interface should be implemented.
     * @param pClientId    Facebook application ID
     * @param pRedirectUri Redirect URL
     */
    public FacebookSocialObject(SocialCallback pSocialCallback, String pClientId, String pRedirectUri) {
        mSocialCallback = pSocialCallback;
        mClientId = pClientId;
        mRedirectUri = pRedirectUri;
    }


    @Override
    public Boolean isParseResponseSuccess(String response) {
        Log.d("facebook", response);
        if (response.contains(ACCESS_TOKEN) && (!response.contains(ERROR_CONST))) {
            String [] result = response.split(ACCESS_TOKEN+"=");
            mToken = result[1];
            Log.d("facebook", mToken);
            Bundle fbBundle = new Bundle();
            fbBundle.putString(ACCESS_TOKEN, mToken);
            mSocialCallback.isSucceed(fbBundle);
            return true;
        } else{
            Bundle errorBundle = new Bundle();
            errorBundle.putString(ERROR_CONST, response);
//            mSocialCallback.isFailed(errorBundle);
            return false;
        }
    }

    @Override
    public String getUrl() {

        return "https://m.facebook.com/dialog/oauth/?client_id=" + mClientId + "&redirect_uri=" + mRedirectUri + "&response_type=token&scope=email&display=wap";
    }

    @Override
    public SocialUser getUser(String pToken) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://graph.facebook.com/me?access_token=" + mToken);

                String name = null;
                String surname = null;
                String email = null;

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    JSONObject resultJson = new JSONObject(result);
                    Log.d("facebook", resultJson.toString());

                    name = resultJson.getString(FIRST_NAME);
                    surname = resultJson.getString(LAST_NAME);

                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                socialUser = new SocialUser(name, surname, email);
                Bundle fbBundle = new Bundle();
                fbBundle.putParcelable(USER_BUNDLE, socialUser);
                mSocialCallback.isSucceed(fbBundle);
            }
        }).start();
        return socialUser;
    }

}
