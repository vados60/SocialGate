package com.example.sociallib.app.extendedmodel;

import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VkSocialObject extends SocialObject {

    private String mClientId;
    private String mRedirectUri;
    private String mScope;
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    public VkSocialObject(SocialCallback pSocialCallback, String pClientId, String pRedirectUri, String pScope) {
        mSocialCallback = pSocialCallback;
        mClientId = pClientId;
        mRedirectUri = pRedirectUri;
        mScope = pScope;
    }

    @Override
    public Boolean isParseResponseSuccess(String response) {
        if (response.contains(ACCESS_TOKEN) && (!response.contains(ERROR_CONST))) {
            String[] result = response.split(ACCESS_TOKEN + "=");

            Bundle vkBundle = new Bundle();
            vkBundle.putString(ACCESS_TOKEN, result[1]);
            mSocialCallback.isSucceed(vkBundle);
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
        return "https://oauth.vk.com/authorize?client_id=" + mClientId + "&redirect_uri=" + mRedirectUri + "&scope=" + mScope + "&display=mobile&v=5.5&response_type=token";
    }

    @Override
    public void getUser(final String pToken) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.vkontakte.ru/method/getProfiles?uid=me&access_token=" + pToken);

                String name = null;
                String surname = null;
                String email = null;

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    JSONObject resultJson = new JSONObject(result);
                    Log.d("VK", resultJson.toString());
                    JSONArray responseJson = resultJson.optJSONArray("response");
                    JSONObject info = responseJson.getJSONObject(0);
                    name = info.getString(FIRST_NAME).toString();
                    surname = info.getString(LAST_NAME).toString();

                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SocialUser socialUser = new SocialUser(name, surname, email);
                Bundle vkBundle = new Bundle();
                vkBundle.putParcelable(USER_BUNDLE, socialUser);
                mSocialCallback.isSucceed(vkBundle);
            }
        }).start();
    }

}
