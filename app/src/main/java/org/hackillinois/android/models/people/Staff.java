package org.hackillinois.android.models.people;

import org.json.JSONException;
import org.json.JSONObject;

public class Staff extends Person {

    private String year;
    private String company;
    private String jobTitle;

    public Staff(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        year = jsonObject.getString("year");
        company = jsonObject.getString("company");
        jobTitle = jsonObject.getString("job_title");

    }

    public String getYear() {
        return year;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}
