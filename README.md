# SocialGate
Hello everybody. I'm glad to see you here)

In this small project I'd like to skow you hou to authorise in some social network without their heavy SDK, but just through WebView. This sample is written on Java for Android system. I tried make it as much flexible as possible.

In the first version I can present authorization mechanism for 5 social networks: Facebook, Google+, LinkedIn, Instagram and VK.com (famous network for russian-speaking countries).

As you probably know, in order to authorize using OAuth2.0 we need to open WebView, compose appropriate URL, set it into WebView and after inserting login/password you will be redirected to redirect_uri. After redirection becomes possible to extract access_token and some other data abouth this authorization event.

Package 'model' contains 6 files. One generic SocialObject and 5 children. **You should copy those social objects, that you need.**  

Then you should create activity that contains WebView and set its client:


```
#!java

@Override 
protected void onCreate(Bundle savedInstanceState){ 
   mWebView = (WebView) findViewById(R.id.social_login_web_view); 
   mWebView.setWebViewClient(new WebViewClientCallback()); 
} 
private final class WebViewClientCallback extends WebViewClient { 
   ...
}

```

After this we should get SocialObjeсt:


```
#!java

mSocialObject = new FacebookSocialObject( CALLBACK , APP_ID , REDIRECT_URL);
```


Next step: get auth url from the social object and load it with WebView:


```
#!java

mWebView.loadUrl(mSocialObject.getUrl());
```


In current activity you should implement SocialCallback interface. If everything goes rigth, in isSucceed method you will get bundle with access_token and other data.


```
#!java

private final class CallbackReceiver implements SocialObject.SocialCallback{

        @Override
        public void isSucceed(Bundle pUserBundle) {
            Log.e("ACCESS_TOKEN", pUserBundle.getString(SocialConst.ACCESS_TOKEN));
        }

        @Override
        public void isFailed(Bundle pErrorBundle) {
            Log.e("ERROR", pErrorBundle.getString(SocialObject.ERROR_CONST));

        }
    }
```

Also you can try sample progect. In order to do this, clone it and build. Everything should work properly.


Thank you for attention. If you have any questions - feel free to ask:)
