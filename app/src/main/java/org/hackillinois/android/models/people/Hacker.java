package org.hackillinois.android.models.people;

import org.json.JSONException;
import org.json.JSONObject;

public class Hacker extends Person {
    private String school;
    private String year;

    public Hacker(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        school = jsonObject.getString("school");
        year = jsonObject.getString("year");
    }

    public String getYear() {
        return year;
    }

    public String getSchool() {

        return school;
    }
}
