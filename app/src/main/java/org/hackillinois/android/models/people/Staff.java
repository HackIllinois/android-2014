package org.hackillinois.android.models.people;

import org.json.JSONException;
import org.json.JSONObject;

public class Staff extends Mentor {

    private String year;

    public Staff(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        year = jsonObject.getString("year");
    }

    public String getYear() {
        return year;
    }
}
