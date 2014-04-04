package org.hackillinois.android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Status {
    long date;
    String status;

    public Status(JSONObject jsonObject) throws JSONException {
        date = jsonObject.getLong("date");
        status = jsonObject.getString("status");
    }
}
