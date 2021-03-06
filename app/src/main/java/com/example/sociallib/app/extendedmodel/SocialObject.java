package com.example.sociallib.app.extendedmodel;


import android.os.Bundle;

public abstract class SocialObject {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_BUNDLE = "user_bundle";
    public static final String ERROR_CONST = "error";

    protected SocialCallback mSocialCallback;

    /**
     * <p>Parse response with token for Social Network </p>
     *
     * @param response response with token for Social Network
     * @return TRUE if Url parsed successful, FALSE if there is an error
     */
    public abstract Boolean isParseResponseSuccess(String response);


    /**
     * <p>Return authorization Url for Social Network </p>
     *
     * @return authorization Url
     */
    public abstract String getUrl();

    public abstract void getUser(String pToken);

    public interface SocialCallback {
        void isSucceed(Bundle pUserBundle);

        void isFailed(Bundle pErrorBundle);
    }
}
