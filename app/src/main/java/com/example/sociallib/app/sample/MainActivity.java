package com.example.sociallib.app.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sociallib.app.R;
import com.example.sociallib.app.extendedmodel.SocialObject;
import com.example.sociallib.app.extendedmodel.SocialUser;


public class MainActivity extends Activity {

    private final int LOGIN_ACTIVITY_KEY = 1;
    ActionType actionType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        Clicker listener = new Clicker();
        findViewById(R.id.pre_login_activity_layout_facebook_button_token).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_linkedin_button_token).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_instagram_button_token).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_vk_button_token).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_google_plus_button_token).setOnClickListener(listener);

        findViewById(R.id.pre_login_activity_layout_facebook_button_token_user).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_linkedin_button_token_user).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_instagram_button_token_user).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_vk_button_token_user).setOnClickListener(listener);
        findViewById(R.id.pre_login_activity_layout_google_plus_button_token_user).setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (resultCode == RESULT_OK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            if (actionType == ActionType.TOKEN) {
                builder.setTitle("Access token:");
                builder.setMessage(data.getExtras().getBundle(SocialObject.USER_BUNDLE).getString(SocialObject.ACCESS_TOKEN));
            } else {
                Bundle b = data.getParcelableExtra(SocialObject.USER_BUNDLE);
                SocialUser mSocialUser = (SocialUser) b.getParcelable(SocialObject.USER_BUNDLE);
                builder.setTitle("Your user name:");
                builder.setMessage(mSocialUser.getName());
            }
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Log.e(SocialObject.ERROR_CONST, data.getExtras().getBundle(SocialObject.ERROR_CONST).getString(SocialObject.ERROR_CONST));
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            SocialType socialType = null;
            switch (v.getId()) {

                case R.id.pre_login_activity_layout_facebook_button_token:
                    socialType = SocialType.FACEBOOK;
                    actionType = ActionType.TOKEN;
                    break;

                case R.id.pre_login_activity_layout_linkedin_button_token:
                    socialType = SocialType.LINKEDIN;
                    actionType = ActionType.TOKEN;
                    break;

                case R.id.pre_login_activity_layout_instagram_button_token:
                    socialType = SocialType.INSTAGRAM;
                    actionType = ActionType.TOKEN;
                    break;

                case R.id.pre_login_activity_layout_vk_button_token:
                    socialType = SocialType.VK;
                    actionType = ActionType.TOKEN;
                    break;

                case R.id.pre_login_activity_layout_google_plus_button_token:
                    socialType = SocialType.GOOGLE_PLUS;
                    actionType = ActionType.TOKEN;
                    break;

                case R.id.pre_login_activity_layout_facebook_button_token_user:
                    socialType = SocialType.FACEBOOK;
                    actionType = ActionType.USER_TOKEN;
                    break;

                case R.id.pre_login_activity_layout_linkedin_button_token_user:
                    socialType = SocialType.LINKEDIN;
                    actionType = ActionType.USER_TOKEN;
                    break;

                case R.id.pre_login_activity_layout_instagram_button_token_user:
                    socialType = SocialType.INSTAGRAM;
                    actionType = ActionType.USER_TOKEN;
                    break;

                case R.id.pre_login_activity_layout_vk_button_token_user:
                    socialType = SocialType.VK;
                    actionType = ActionType.USER_TOKEN;
                    break;

                case R.id.pre_login_activity_layout_google_plus_button_token_user:
                    socialType = SocialType.GOOGLE_PLUS;
                    actionType = ActionType.USER_TOKEN;
                    break;

            }
            Intent intent = SocialUtils.loginSocial(MainActivity.this, socialType, actionType);
            startActivityForResult(intent, LOGIN_ACTIVITY_KEY);
        }
    }
}
