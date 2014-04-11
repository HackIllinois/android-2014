package org.hackillinois.android.models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author vishal
 */
public class ScheduleItem implements Comparable<ScheduleItem>{

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

    public int getHour() {
        return time_hour(time);
    }

    public int getMinute() {
        return time_minute(time);
    }

    public int getDay() {
        return format_day(time);
    }

    public String getIconURL() {
        return iconURL;
    }

    private String format_time(int unixTime) {
        DateTime localTime = new DateTime((long) unixTime * 1000);
        DateTimeFormatter formatter = DateTimeFormat.shortTime();
        return localTime.toString(formatter);
    }

    private int format_day(int unixTime) {
        DateTime time = new DateTime((long) unixTime * 1000);
        return time.getDayOfMonth();
    }

    private int time_hour(int unixTime) {
        DateTime time = new DateTime((long) unixTime * 1000);
        return time.getHourOfDay();
    }

    private int time_minute(int unixTime) {
        DateTime time = new DateTime((long) unixTime * 1000);
        return time.getMinuteOfHour();
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(ScheduleItem another) {
        if(this.getHour() < another.getHour())
            return -1;
        if(this.getHour() > another.getHour())
            return 1;
        else return 0;
    }
}
