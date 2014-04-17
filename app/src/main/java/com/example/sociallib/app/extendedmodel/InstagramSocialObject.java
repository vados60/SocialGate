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


public class InstagramSocialObject extends SocialObject {

    private static final String DATA = "data";
    private static final String FULL_NAME = "full_name";
    private String mClientId;
    private String mRedirectUri;

    /**
     * @param pSocialCallback Callback object. SocialCallback interface should be implemented.
     * @param pClientId    Instagram application ID
     * @param pRedirectUri Redirect URL
     */
    public InstagramSocialObject(SocialCallback pSocialCallback, String pClientId, String pRedirectUri) {
        mClientId = pClientId;
        mRedirectUri = pRedirectUri;
        mSocialCallback = pSocialCallback;
    }

    @Override
    public Boolean isParseResponseSuccess(String response) {
        if (response.contains(ACCESS_TOKEN) && (!response.contains(ERROR_CONST))) {

            String [] result = response.split(ACCESS_TOKEN+"=");

            Bundle b = new Bundle();
            b.putString(ACCESS_TOKEN, result[1]);
            mSocialCallback.isSucceed(b);
            return true;
        } else if (response.contains("error")) {
            Bundle errorBundle = new Bundle();
            errorBundle.putString(ERROR_CONST, response);
            mSocialCallback.isFailed(errorBundle);
            return false;
        }
        return false;
    }

    @Override
    public String getUrl() {
        return "https://api.instagram.com/oauth/authorize/?client_id=" + mClientId + "&redirect_uri=" + mRedirectUri + "&response_type=token";
    }

    @Override
    public void getUser(final String pToken) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.instagram.com/v1/users/self/?access_token=" + pToken);

                String name = null;
                String surname = null;
                String email = null;

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    JSONObject resultJson = new JSONObject(result);
                    Log.d("instagram", resultJson.toString());

                    JSONObject data = resultJson.getJSONObject(DATA);
                    String fullName = data.getString(FULL_NAME);
                    name = fullName.substring(0, fullName.indexOf(" "));
                    surname = fullName.substring(fullName.indexOf(" ") + 1);

                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SocialUser socialUser = new SocialUser(name, surname, email);
                Bundle fbBundle = new Bundle();
                fbBundle.putParcelable(USER_BUNDLE, socialUser);
                mSocialCallback.isSucceed(fbBundle);
            }
        }).start();
    }

}
