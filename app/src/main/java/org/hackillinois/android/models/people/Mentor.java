package org.hackillinois.android.models.people;

import org.json.JSONException;
import org.json.JSONObject;

public class Mentor extends Person {

    private String company;
    private String jobTitle;

    public Mentor(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        company = jsonObject.getString("company");
        jobTitle = jsonObject.getString("job_title");
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}
