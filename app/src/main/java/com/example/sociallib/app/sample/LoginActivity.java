package com.example.sociallib.app.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.sociallib.app.R;
import com.example.sociallib.app.extendedmodel.SocialObject;
import com.example.sociallib.app.extendedmodel.SocialUser;


public class LoginActivity extends Activity {


    private WebView mWebView;
    private SocialObject mSocialObject;
    private ActionType mActionType;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        mWebView = (WebView) findViewById(R.id.social_login_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClientCallback());
        mSocialObject = SocialFactory.getSocialObject((SocialType) getIntent().getExtras().getSerializable(SocialUtils.SOCIAL_TYPE), new CallbackReceiver());
        mActionType = (ActionType) getIntent().getExtras().getSerializable(SocialUtils.ACTION_TYPE);
        mWebView.loadUrl(mSocialObject.getUrl());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private final class WebViewClientCallback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (mSocialObject.isParseResponseSuccess(url)) {
                //do smth
//                mSocialObject.getUser();
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
            // do your stuff here
            mProgressDialog.dismiss();
        }
    }

    private final class CallbackReceiver implements SocialObject.SocialCallback {

        @Override
        public void isSucceed(Bundle pUserBundle) {
            //Log.e("ACCESS_TOKEN", pUserBundle.getString(SocialObject.ACCESS_TOKEN));

            Intent intent = new Intent();
            if (mActionType == ActionType.USER_TOKEN) {
                mActionType = ActionType.TOKEN;
                mSocialObject.getUser(pUserBundle.getString(SocialObject.ACCESS_TOKEN));
            } else {
                intent.putExtra(SocialObject.USER_BUNDLE, pUserBundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        @Override
        public void isFailed(Bundle pErrorBundle) {
            Log.e("ERROR", pErrorBundle.getString(SocialObject.ERROR_CONST));
            Intent intent = new Intent();
            intent.putExtra(SocialObject.ERROR_CONST, pErrorBundle);
            setResult(RESULT_CANCELED, intent);
            finish();

        }
    }
}
