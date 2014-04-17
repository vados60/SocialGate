package com.example.sociallib.app.extendedmodel;

import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LinkedinSocialObject extends SocialObject {

    private static final String STATE_PARAM = "state";
    private static final String CODE_PARAM = "code";
    private static final String FIRST_NAME = "first-name";
    private static final String LAST_NAME = "last-name";
    private static final String EMAIL = "email-address";
    private String mApiKey;
    private String mRedirectUri;
    private String mState;
    private String mSecretKey;

    public LinkedinSocialObject(SocialCallback pSocialCallback, String pApiKey, String pRedirectUri, String pState, String pSecretKey) {
        mApiKey = pApiKey;
        mRedirectUri = pRedirectUri;
        mState = pState;
        mSecretKey = pSecretKey;
        mSocialCallback = pSocialCallback;
    }

    @Override
    public Boolean isParseResponseSuccess(String response) {
        if (response.startsWith(mRedirectUri)) {
            Uri uri = Uri.parse(response);
            String stateToken = uri.getQueryParameter(STATE_PARAM);
            if (stateToken == null || !stateToken.equals(mState)) {
                Log.e("Authorize", "State token doesn't match");
                return false;
            }
            String authorizationToken = uri.getQueryParameter(CODE_PARAM);
            if (authorizationToken == null) {
                Log.i("Authorize", "The user doesn't allow authorization.");
                return false;
            }
            Log.i("Authorize", "Auth token received: " + authorizationToken);

            executePostRequest("https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code&code=" + authorizationToken + "&redirect_uri=" + mRedirectUri + "&client_id=" + mApiKey + "&client_secret=" + mSecretKey);
        }
        return true;
    }

    @Override
    public String getUrl() {

        return "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=" + mApiKey + "&state=" + mState + "&redirect_uri=" + mRedirectUri;
    }

    @Override
    public void getUser(final String pToken) {
        
        new Thread(new Runnable() {
            @Override
            public void run() {

                SocialUser socialUser = null;
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)?oauth2_access_token=" + pToken);

                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity());
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(new StringReader(result));

                    socialUser = parseXML(myparser);
//                    Log.e("Linkedin user", "" + socialUser.getName() + " " + socialUser.getSurname() + "  " + socialUser.getEmail());
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                Bundle linkedInBundle = new Bundle();
                linkedInBundle.putParcelable(USER_BUNDLE, socialUser);
                mSocialCallback.isSucceed(linkedInBundle);
            }
        }).start();
    }

    private void executePostRequest(final String pUrl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(pUrl);
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    if (response != null) {
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            JSONObject resultJson = new JSONObject(result);
                            String accessToken = resultJson.has(ACCESS_TOKEN) ? resultJson.getString(ACCESS_TOKEN) : null;
                            Bundle linkedInBundle = new Bundle();
                            linkedInBundle.putString(ACCESS_TOKEN, accessToken);
                            mSocialCallback.isSucceed(linkedInBundle);
                        }
                    } else {
                        Bundle errorBundle = new Bundle();
                        errorBundle.putString(ERROR_CONST, EntityUtils.toString(response.getEntity()));
                        mSocialCallback.isFailed(errorBundle);
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
        }).start();
    }

    private SocialUser parseXML(XmlPullParser pParser) {
        int event;
        String text = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        try {
            event = pParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = pParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = pParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals(FIRST_NAME)) {
                            firstName = text;
                        } else if (name.equals(LAST_NAME)) {
                            lastName = text;
                        } else if (name.equals(EMAIL)) {
                            email = text;
                        }
                        break;
                }
                event = pParser.next();

            }
            return new SocialUser(firstName, lastName, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
