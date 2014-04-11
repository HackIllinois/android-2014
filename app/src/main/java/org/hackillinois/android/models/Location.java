package org.hackillinois.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rlemie2 on 4/10/14.
 */
public class Location implements Serializable {
    String building;
    String floor;
    String room_number;
    String room_type;
    String room_name;
    String image_url;

    public Location(JSONObject json) throws JSONException{
        building = json.getString("building");
        floor = json.getString("floor");
        room_name = json.getString("room_name");
        room_number = json.getString("room_number");
        room_type = json.getString("room_type");
        image_url = json.getString("image_url");

    }

    public String toString(){
        return building + " " + room_number;
    }


}
