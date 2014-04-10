package org.hackillinois.android.profile;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import org.hackillinois.android.models.Status;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 4/9/14.
 */
public class StatusDataLoader extends AsyncTaskLoader<List<Status>> {


    private URL url;
    private Context mContext;

    public StatusDataLoader(Context context, URL url) {
        super(context);
        mContext = context;
        this.url = url;
    }

    /**
     */
    @Override
    public List<Status> loadInBackground() {
        String data = null; // this will hold the JSON in a string representation
        // try loading the URL
        try{
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null){
            List<Status> statusList = new ArrayList<Status>();
            try {
                // gets the json object that has 3 arrays for the days
                final JSONObject jsonObject = new JSONObject(data);
                final JSONArray jsonArray = jsonObject.getJSONArray(data); // get the json array for this day

                // get all the schedule items for this day that's stored in the backend
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject status= jsonArray.getJSONObject(i);
                    // add each schedule item retrieved from the backend to the array list
//                    statusList.add(status);
                }

                return statusList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
