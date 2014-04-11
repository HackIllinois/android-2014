package org.hackillinois.android.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Status;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Jonathan on 4/9/14.
 */
public class StatusDataLoader  extends AsyncTaskLoader<Person> {

    private Context mContext;
    private Status status;

    public StatusDataLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Person loadInBackground() {
        String data = null;
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String email = sharedPreferences.getString(mContext.getString(R.string.pref_email), "");
            if(email.equals(""))
                data = null;
            else
                data = httpUtils.testEmail(email);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {

            try {
                JSONArray jsonArray = new JSONArray(data);
                JSONObject json = jsonArray.getJSONObject(0);
                String type = json.getString("type");
                Person person;
                if (type.equals("hacker")) {
                    person = new Hacker(json);
                } else if (type.equals("staff")) {
                    person = new Staff(json);
                } else if (type.equals("mentor")) {
                    person = new Mentor(json);
                } else {
                    person = null;
                }

                return person;

            } catch(JSONException j) {
                j.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
