package com.example.sociallib.app.sample;

import com.example.sociallib.app.extendedmodel.FacebookSocialObject;
import com.example.sociallib.app.extendedmodel.GoogleSocialObject;
import com.example.sociallib.app.extendedmodel.InstagramSocialObject;
import com.example.sociallib.app.extendedmodel.LinkedinSocialObject;
import com.example.sociallib.app.extendedmodel.SocialObject;
import com.example.sociallib.app.extendedmodel.VkSocialObject;


public class SocialFactory {

    /**
     * <p>Create SocialObject depending on the input parameter(enum) </p>
     *
     * @param pSocialType Type of required Social Network (enum)
     * @return specific SocialObject
     * @see com.example.sociallib.app.extendedmodel.SocialObject
     */

    public static SocialObject getSocialObject(SocialType pSocialType, SocialObject.SocialCallback pSocialCallback) {
        switch (pSocialType) {

            case VK:
                return new VkSocialObject(pSocialCallback, "4090078", "https://oauth.vk.com/blank.html", "friends,messages");

            case INSTAGRAM:
                return new InstagramSocialObject(pSocialCallback, "941ecbadcc4e4f989ec4a959da9d08ba", "http://null.com");

            case LINKEDIN:
                return new LinkedinSocialObject(pSocialCallback, "7537riy2vq2sxv", "http://null.com", "DCEEFWF45453sdffef424", "AitA0VM2ZcLTdU19");

            case FACEBOOK:
                return new FacebookSocialObject(pSocialCallback, "426034244199496", "http://null.com");

            case GOOGLE_PLUS:
                return new GoogleSocialObject(pSocialCallback, "1033746217948-p5jn9kgocbbd09c5h7e76tb9152ak5jl.apps.googleusercontent.com", "https://www.example.com/oauth2callback");
        }

        return null;
    }


}
