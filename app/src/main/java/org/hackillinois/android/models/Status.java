package org.hackillinois.android.models;

import org.joda.time.DateTime;
import org.joda.time.Period;
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

    public String getDate() {
        return format_time(date);
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

    private String format_time(long unixTime) {
        DateTime time = new DateTime((long) unixTime*1000);
        DateTime now = new DateTime();
        Period diff = new Period(time, now);

        if(diff.getYears() > 0)
            return diff.getYears() + "years ago";
        else if(diff.getMonths() > 0)
            return diff.getMonths() + "months ago";
        else if(diff.getWeeks() > 0)
            return diff.getWeeks() + "w ago";
        else if(diff.getDays() > 0)
            return diff.getDays() + "d ago";
        else if(diff.getHours() > 0)
            return diff.getHours() + "h ago";
        else if(diff.getMinutes() == 0)
            return "just now";
        else
            return diff.getMinutes() + "m ago";
    }
}
