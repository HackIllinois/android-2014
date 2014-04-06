package org.hackillinois.android;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.Utils.Utils;
import org.hackillinois.android.models.ScheduleItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vishal
 */
public class ScheduleDataLoader extends AsyncTaskLoader<List<ScheduleItem>> {

    private URL url;
    private String day;

    public ScheduleDataLoader(Context context, URL url, String day) {
        super(context);
        this.url = url;
        this.day = day;
    }

    /**
     */
    @Override
    public List<ScheduleItem> loadInBackground() {
        String data = null; // this will hold the JSON in a string representation
        // try loading the URL
        try{
           data = Utils.loadData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null){
            List<ScheduleItem> scheduleList = new ArrayList<ScheduleItem>();
            try {
                // gets the json object that has 3 arrays for the days
                final JSONObject jsonObject = new JSONObject(data);
                final JSONArray jsonArray = jsonObject.getJSONArray(day); // get the json array for this day

                // get all the schedule items for this day that's stored in the backend
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject schedule = jsonArray.getJSONObject(i);
                    // add each schedule item retrieved from the backend to the array list
                    scheduleList.add(new ScheduleItem(schedule));
                }

                return scheduleList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
