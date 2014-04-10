package org.hackillinois.android.models.people;

import org.hackillinois.android.models.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Person implements Serializable {

    private String name;
    private String email;
    private String homebase;
    private List<String> skills;
    private String fbID;
    private List<Status> statuses;
    private String type;
    private long time;
    private int databaseKey;
    private String mac;
    private int RSSI;

    public Person(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        email = jsonObject.getString("email");
        if (jsonObject.has("homebase")) {
            homebase = jsonObject.getString("homebase");
        }
        JSONArray skillsArray = jsonObject.getJSONArray("skills");
        skills = new ArrayList<String>();
        for(int i = 0; i < skillsArray.length(); i++) {
            skills.add(skillsArray.getString(i));
        }

        fbID = jsonObject.getString("fb_url");
        JSONArray statusArray = jsonObject.getJSONArray("status");
        statuses = new ArrayList<Status>();
        for (int i = 0; i < statusArray.length(); i++) {
            JSONObject status = statusArray.getJSONObject(i);
            statuses.add(new Status(status));

        }
        type = jsonObject.getString("type");
        //time = jsonObject.getLong("time");
        databaseKey = jsonObject.getInt("database_key");
        //mac = jsonObject.getString("mac_address");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHomebase() {
        return homebase;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getFbID() {
        return fbID;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public int getDatabaseKey() {
        return databaseKey;
    }

    public String getMac() {
        return mac;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }
}
