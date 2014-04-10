package org.hackillinois.android.people;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.SparseArray;

import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PersonDataLoader extends AsyncTaskLoader<PeopleDataHolder> {

    private URL urlToLoad;
    private Context mContext;

    public PersonDataLoader(Context context, URL url) {
        super(context);
        urlToLoad = url;
        mContext = context;
    }

    @Override
    public PeopleDataHolder loadInBackground() {
        String data = null;
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(urlToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
            PeopleDataHolder peopleDataHolder = new PeopleDataHolder();
            List<Hacker> hackers = new ArrayList<Hacker>();
            List<Mentor> mentors = new ArrayList<Mentor>();
            SparseArray<Person> iOSLookup = new SparseArray<Person>();
            HashMap<String, Person> androidLookup = new HashMap<String, Person>();
            peopleDataHolder.setHackerList(hackers);
            peopleDataHolder.setMentorAndStaffList(mentors);
            peopleDataHolder.setiOSMap(iOSLookup);
            peopleDataHolder.setAndroidMap(androidLookup);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject person = jsonArray.getJSONObject(i);
                    String type = person.getString("type");
                    if (type.equals("hacker")) {
                        Hacker hacker = new Hacker(person);
                        iOSLookup.put(hacker.getDatabaseKey(), hacker);
                        androidLookup.put(hacker.getMac(), hacker);
                        hackers.add(hacker);
                    } else if (type.equals("staff")) {
                        Staff staff = new Staff(person);
                        iOSLookup.put(staff.getDatabaseKey(), staff);
                        androidLookup.put(staff.getMac(), staff);
                        mentors.add(staff);
                    } else if (type.equals("mentor")) {
                        Mentor mentor = new Mentor(person);
                        iOSLookup.put(mentor.getDatabaseKey(), mentor);
                        androidLookup.put(mentor.getMac(), mentor);
                        mentors.add(mentor);
                    }
                }
                Collections.sort(hackers);
                Collections.sort(mentors);
                return peopleDataHolder;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
