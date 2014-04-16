package com.example.sociallib.app.extendedmodel;

import android.os.Bundle;


public class InstagramSocialObject extends SocialObject {

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
            Bundle b = new Bundle();
            b.putString(ACCESS_TOKEN, response);
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
    public SocialUser getUser() {
        return null;
    }

}
