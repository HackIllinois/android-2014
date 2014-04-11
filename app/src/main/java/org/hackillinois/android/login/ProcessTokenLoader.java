package org.hackillinois.android.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

public class ProcessTokenLoader extends AsyncTaskLoader<Person> {

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
    public Person loadInBackground() {
        if (mUrl.startsWith(Oauth2Params.GOOGLE_PLUS.getRedirectUri())) {
            try {
                if (mUrl.contains("code=")) {
                    String authorizationCode = extractCodeFromUrl(mUrl);
                    mOAuth2Helper.retrieveAndStoreAccessToken(authorizationCode);

                    HttpUtils httpUtils = HttpUtils.getHttpUtils(getContext());
                    String apiCall = mOAuth2Helper.executeApiCall();
                    JSONObject response = new JSONObject(apiCall);
                    JSONArray array = response.getJSONArray("emails");

                    for (int i = 0; i < array.length(); i ++) {
                        String email = array.getJSONObject(i).getString("value");
                        String userResponse = httpUtils.testEmail(email);
                        if (!userResponse.equals("[]")) {
                            Person resPerson = null;
                            JSONArray jsonArray = new JSONArray(userResponse);
                            JSONObject person = jsonArray.getJSONObject(0);
                            String type = person.getString("type");
                            if (type.equals("hacker")) {
                                resPerson = new Hacker(person);
                            } else if (type.equals("staff")) {
                                resPerson = new Staff(person);
                            } else if (type.equals("mentor")) {
                                resPerson = new Staff(person);
                            }
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(getContext().getString(R.string.pref_email), email);
                            editor.commit();
                            return resPerson;
                        }
                    }
                    return null;
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
