package org.hackillinois.android.models;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author vishal
 */
public class ScheduleItem {

    private String eventName;
    private String description;
    private String building;
    private String floor;
    private String roomNumber;
    private String roomType;
    private String roomName;
    private String imageURL;
    private int time;
    private String iconURL;

    public ScheduleItem(JSONObject jsonObject) throws JSONException {
        eventName = jsonObject.getString("event_name");
        description = jsonObject.getString("description");

        // The building, floor, roomNum, roomType, roomName, and imageURL are all in the location
        // JSON object that's encapsulated within the @param jsonObject JSONObject
        final JSONObject location = jsonObject.getJSONObject("location");
        building = location.getString("building");
        floor = location.getString("floor");
        roomNumber = location.getString("room_number");
        roomType = location.getString("room_type");
        roomName = location.getString("room_name");
        imageURL = location.getString("image_url");

        time = jsonObject.getInt("time");
        iconURL = jsonObject.getString("icon_url");
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTime() {
        return format_time(time);
    }

    public String getIconURL() {
        return iconURL;
    }

    private String format_time(int unixTime) {
        DateTime time = new DateTime((long) unixTime*1000);
        DateTime now = new DateTime();
        Period diff = new Period(now, time);

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
