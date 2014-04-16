package com.example.sociallib.app.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sociallib.app.R;
import com.example.sociallib.app.extendedmodel.SocialObject;


public class MainActivity extends Activity {

    private final int LOGIN_ACTIVITY_KEY = 1;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (resultCode == RESULT_OK) {
            Log.e(SocialObject.ACCESS_TOKEN, data.getStringExtra(SocialObject.ACCESS_TOKEN));
        } else {
            Log.e(SocialObject.ERROR_CONST, data.getStringExtra(SocialObject.ERROR_CONST));
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            SocialType socialType = null;
            switch (v.getId()) {


                case R.id.pre_login_activity_layout_facebook_button_token:
                    socialType = SocialType.FACEBOOK;
                    break;

                case R.id.pre_login_activity_layout_linkedin_button_token:
                    socialType = SocialType.LINKEDIN;
                    break;

                case R.id.pre_login_activity_layout_instagram_button_token:
                    socialType = SocialType.INSTAGRAM;
                    break;

                case R.id.pre_login_activity_layout_vk_button_token:
                    socialType = SocialType.VK;
                    break;

                case R.id.pre_login_activity_layout_google_plus_button_token:
                    socialType = SocialType.GOOGLE_PLUS;
                    break;

            }
            Intent intent = SocialUtils.loginSocial(getApplicationContext(), socialType);
            startActivityForResult(intent, LOGIN_ACTIVITY_KEY);
        }
    }
}
