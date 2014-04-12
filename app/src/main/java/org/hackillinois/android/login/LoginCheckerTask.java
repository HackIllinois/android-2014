package org.hackillinois.android.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.R;
import org.hackillinois.android.utils.HttpUtils;
import org.hackillinois.android.utils.Utils;

import java.io.IOException;
import java.util.List;

public class LoginCheckerTask extends AsyncTaskLoader<String> {
    SharedPreferences mSharedPreferences;
    Context mContext;

    public LoginCheckerTask(Context context) {
        super(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }

    @Override
    public String loadInBackground() {
        List<String> emails = Utils.getUserEmails(mContext);
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            for (String email : emails) {
                String userResponse = httpUtils.testEmail(email);
                if (!userResponse.equals("[]")) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(getContext().getString(R.string.pref_email), email);
                    editor.commit();
                    return email;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
