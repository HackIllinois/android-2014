package org.hackillinois.android.models;

import org.hackillinois.android.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author vishal
 */
public class ScheduleItem implements Comparable<ScheduleItem>{

    private static int[] images = {
            R.drawable.ic_hackillinois_schedule,
            R.drawable.ic_inin,
            R.drawable.ic_pebble,
            R.drawable.ic_3red,
            R.drawable.ic_westmonroe,
            R.drawable.ic_answers,
            R.drawable.ic_imo,
            R.drawable.ic_goldmansachs,
            R.drawable.ic_bloomberg,
            R.drawable.ic_niksun,
            R.drawable.ic_ge,
            R.drawable.ic_wolfram,
            R.drawable.ic_groupon,
            R.drawable.ic_allston,
            R.drawable.ic_yahoo,
            R.drawable.ic_trunkclub
    };

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
    private double latitude;
    private double longitude;
    private int mDrawableInt = 0;

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

        // since the backend wont give us what we want, the following are HACKS
        if (roomNumber.equals("SC 0216")) {
            latitude = 40.113760;
            longitude = -88.225017;
        } else if (roomNumber.equals("SC 0224")) {
            latitude = 40.113772;
            longitude = -88.224636;
        } else if (roomNumber.equals("SC Basement")) {
            latitude = 40.113870;
            longitude = -88.224848;
        } else if (roomNumber.equals("SC 1103")) {
            latitude = 40.113964;
            longitude = -88.225296;
        } else if (roomNumber.equals("SC 1105")) {
            latitude = 40.114066;
            longitude = -88.225294;
        } else if (roomNumber.equals("SC 1109")) {
            latitude = 40.114167;
            longitude = -88.225295;
        } else if (roomNumber.equals("SC 1112")) {
            latitude = 40.114301;
            longitude = -88.225133;
        } else if (roomNumber.equals("SC 1131")) {
            latitude = 40.114276;
            longitude = -88.225307;
        } else if (roomNumber.equals("SC 1214")) {
            latitude = 40.113791;
            longitude = -88.224911;
        } else if (roomNumber.equals("SC 1302")) {
            latitude = 40.113794;
            longitude = -88.224741;
        } else if (roomNumber.equals("SC 1304")) {
            latitude = 40.113793;
            longitude = -88.224603;
        } else if (roomNumber.equals("SC 1404")) {
            latitude = 40.114056;
            longitude = -88.224460;
        } else if (roomNumber.equals("SC Atrium")) {
            latitude = 40.113886;
            longitude = -88.224955;
        } else if (roomNumber.equals("SC 1312")) {
            latitude = 40.113719;
            longitude = -88.224829;
        } else if (roomNumber.equals("SC 2405")) {
            latitude = 40.113990;
            longitude = -88.224428;
        } else if (roomNumber.equals("SC 2102")) {
            latitude = 40.113928;
            longitude = -88.225188;
        } else if (roomNumber.equals("SC 2124")) {
            latitude = 40.114299;
            longitude = -88.225139;
        } else if (roomNumber.equals("SC 3401")) {
            latitude = 40.113976;
            longitude = -88.224555;
        } else if (roomNumber.equals("SC 3403")) {
            latitude = 40.113986;
            longitude = -88.224469;
        } else if (roomNumber.equals("SC 3405")) {
            latitude = 40.113994;
            longitude = -88.224380;
        } else if (roomNumber.equals("SC 3102")) {
            latitude = 40.113925;
            longitude = -88.225189;
        } else if (roomNumber.equals("SC 3124")) {
            latitude = 40.114298;
            longitude = -88.225131;
        } else if (roomNumber.equals("SC 4102")) {
            latitude = 40.113929;
            longitude = -88.225190;
        } else if (roomNumber.equals("SC 4124")) {
            latitude = 40.114301;
            longitude = -88.225132;
        } else if (roomNumber.equals("SC 4403")) {
            latitude = 40.114001;
            longitude = -88.224473;
        } else if (roomNumber.equals("SC 4405")) {
            latitude = 40.114001;
            longitude = -88.224383;
        } else if (roomNumber.equals("SC 4407")) {
            latitude = 40.114014;
            longitude = -88.224293;
        } else if (roomNumber.equals("DCL 1310")) {
            latitude = 40.113185;
            longitude = -88.225861;
        } else if (roomNumber.equals("DCL 1320")) {
            latitude = 40.113210;
            longitude = -88.226039;
        } else if (roomNumber.equals("DCL 2320")) {
            latitude = 40.113228;
            longitude = -88.226011;
        } else if (roomNumber.equals("DCL 2436")) {
            latitude = 40.113116;
            longitude = -88.226112;
        } else {
            latitude = 40.114300;
            longitude = -88.224837;
        }

        if (iconURL.contains("hackillinois.png")) {
            mDrawableInt = 0;
        } else if (iconURL.contains("inin.png")) {
            mDrawableInt = 1;
        } else if (iconURL.contains("pebble.png")) {
            mDrawableInt = 2;
        } else if (iconURL.contains("3red.png")) {
            mDrawableInt = 3;
        } else if (iconURL.contains("westmonroe.png")) {
            mDrawableInt = 4;
        } else if (iconURL.contains("answers.png")) {
            mDrawableInt = 5;
        } else if (iconURL.contains("imo.png")) {
            mDrawableInt = 6;
        } else if (iconURL.contains("goldman.png")) {
            mDrawableInt = 7;
        } else if (iconURL.contains("bloomberg.png")) {
            mDrawableInt = 8;
        } else if (iconURL.contains("niksun.png")) {
            mDrawableInt = 9;
        } else if (iconURL.contains("ge.png")) {
            mDrawableInt = 10;
        } else if (iconURL.contains("wolfram.png")) {
            mDrawableInt = 11;
        } else if (iconURL.contains("groupon.png")) {
            mDrawableInt = 12;
        } else if (iconURL.contains("allston.png")) {
            mDrawableInt = 13;
        } else if (iconURL.contains("yahoo.png")) {
            mDrawableInt = 14;
        } else if (iconURL.contains("trunkclub.png")) {
            mDrawableInt = 15;
        }
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

    public double getRoomLong() {
        return longitude;
    }

    public double getRoomLat() {
        return latitude;
    }

    public int getImageDrawable() {
         return images[mDrawableInt];
    }

    @Override
    public int compareTo(ScheduleItem another) {
        return time - another.time;
    }
}
