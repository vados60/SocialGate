package com.example.sociallib.app.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.sociallib.app.R;
import com.example.sociallib.app.extendedmodel.SocialObject;


public class LoginActivity extends Activity {


    private WebView mWebView;
    private SocialObject mSocialObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        mWebView = (WebView) findViewById(R.id.social_login_web_view);

        mWebView.setWebViewClient(new WebViewClientCallback());
        mSocialObject = SocialFactory.getSocialObject((SocialType) getIntent().getExtras().getSerializable(SocialUtils.TYPE), new CallbackReceiver());
        mWebView.loadUrl(mSocialObject.getUrl());

    }

    private final class WebViewClientCallback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (mSocialObject.isParseResponseSuccess(url)){
                //do smth
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }
    }

    private final class CallbackReceiver implements SocialObject.SocialCallback{

        @Override
        public void isSucceed(Bundle pUserBundle) {
//            Log.e("ACCESS_TOKEN", pUserBundle.getString(SocialObject.ACCESS_TOKEN));
            Intent intent = new Intent();
            intent.putExtra(SocialObject.ACCESS_TOKEN, pUserBundle);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void isFailed(Bundle pErrorBundle) {
//            Log.e("ERROR", pErrorBundle.getString(SocialObject.ERROR_CONST));
            Intent intent = new Intent();
            intent.putExtra(SocialObject.ERROR_CONST, pErrorBundle);
            setResult(RESULT_CANCELED, intent);
            finish();

        }
    }
}
