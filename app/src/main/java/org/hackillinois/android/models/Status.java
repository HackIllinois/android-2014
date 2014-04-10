package org.hackillinois.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Status implements Serializable {
    long date;
    String status;

    public Status(JSONObject jsonObject) throws JSONException {
        date = jsonObject.getLong("date");
        status = jsonObject.getString("status");
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
