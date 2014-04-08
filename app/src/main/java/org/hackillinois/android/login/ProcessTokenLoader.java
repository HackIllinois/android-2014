package org.hackillinois.android.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.hackillinois.android.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

public class ProcessTokenLoader extends AsyncTaskLoader<String> {

    private String mUrl;
    private SharedPreferences mSharedPreferences;
    private OAuth2Helper mOAuth2Helper;

    private static final String TAG = "ProcessTokenLoader";

    public ProcessTokenLoader(Context context, String url) {
        super(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mOAuth2Helper = new OAuth2Helper(mSharedPreferences);
        mUrl = url;
    }

    private String extractCodeFromUrl(String url) throws Exception {
        String encodedCode = url.substring(Oauth2Params.GOOGLE_PLUS.getRedirectUri().length()+7,url.length());
        return URLDecoder.decode(encodedCode, "UTF-8");
    }

    @Override
    public String loadInBackground() {
        if (mUrl.startsWith(Oauth2Params.GOOGLE_PLUS.getRedirectUri())) {
            try {
                if (mUrl.contains("code=")) {
                    String authorizationCode = extractCodeFromUrl(mUrl);
                    mOAuth2Helper.retrieveAndStoreAccessToken(authorizationCode);


                    String apiCall = mOAuth2Helper.executeApiCall();
                    JSONObject response = new JSONObject(apiCall);
                    JSONArray array = response.getJSONArray("emails");
                    ArrayList<String> emails = new ArrayList<String>();
                    for (int i = 0; i < array.length(); i ++) {
                        emails.add(array.getJSONObject(i).getString("value"));
                    }
                    //TODO check all the emails for the correct one
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(getContext().getString(R.string.pref_email), emails.get(0));
                    editor.commit();

                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("LOGGED_IN"));
                    return emails.get(0);
                } else if (mUrl.contains("error=")) {
                    Log.i(TAG, "error is " + mUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Not doing anything for mUrl " + mUrl);
        }
        return null;
    }
}
