package com.example.sociallib.app.sample;

import android.content.Context;
import android.content.Intent;

final class SocialUtils {

    public static final String SOCIAL_TYPE = "social_type";
    public static final String ACTION_TYPE = "action_type";

    /**
     * <p>Collect the Intent, that launches WebView, with the required type of Social Network </p>
     *
     * @param pSocialType Type of required Social Network (enum)
     * @param pActionType Type of action needed (enum)
     * @return collected intent
     */

    static Intent loginSocial(Context pContext, SocialType pSocialType, ActionType pActionType) {

        Intent intent = new Intent(pContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(SOCIAL_TYPE, pSocialType);
        intent.putExtra(ACTION_TYPE, pActionType);

        return intent;
    }
}
