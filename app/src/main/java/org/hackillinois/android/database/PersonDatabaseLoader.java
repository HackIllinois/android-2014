package org.hackillinois.android.database;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class PersonDatabaseLoader extends AsyncTaskLoader<Void> {
    private static final String PEOPLE_URL = "http://hackillinois.org/mobile/person";

    Context mContext;
    DatabaseTable mDatabaseTable;

    public PersonDatabaseLoader(Context context) {
        super(context);
        mContext = context;
        mDatabaseTable = new DatabaseTable(context);
    }

    @Override
    public Void loadInBackground() {
        String data = null;
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(new URL(PEOPLE_URL));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject person = jsonArray.getJSONObject(i);
                    String type = person.getString("type");
                    if (type.equals("hacker")) {
                        Hacker hacker = new Hacker(person);
                        mDatabaseTable.addHacker(hacker);
                    } else if (type.equals("mentor") || type.equals("staff")) {
                        Mentor mentor = new Mentor(person);
                        mDatabaseTable.addMentor(mentor);
                    }
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
