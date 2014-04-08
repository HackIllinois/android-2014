package org.hackillinois.android.people;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.utils.HttpUtils;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonDataLoader extends AsyncTaskLoader<List<Person>> {

    private URL urlToLoad;
    private Context mContext;

    public PersonDataLoader(Context context, URL url) {
        super(context);
        urlToLoad = url;
        mContext = context;
    }

    @Override
    public List<Person> loadInBackground() {
        String data = null;
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(urlToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
            List<Person> persons = new ArrayList<Person>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject person = jsonArray.getJSONObject(i);
                    String type = person.getString("type");
                    if (type.equals("hacker")) {
                        persons.add(new Hacker(person));
                    } else if (type.equals("staff")) {
                        persons.add(new Staff(person));
                    } else if (type.equals("mentor")) {
                        persons.add(new Mentor(person));
                    }
                }
                return persons;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}