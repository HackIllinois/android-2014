package org.hackillinois.android.profile;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.models.Location;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlemie2 on 4/10/14.
 */
public class LocationDataLoader extends AsyncTaskLoader<List<Location>> {

    private URL url;
    private Context mContext;

    public LocationDataLoader(Context context) throws MalformedURLException {
        super(context);
        mContext = context;
        this.url = new URL("http://hackillinois.org/mobile/map");
    }

    /**
     */
    @Override
    public List<Location> loadInBackground() {
        String data = null; // this will hold the JSON in a string representation
        // try loading the URL
        try{
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null){
            List<Location> locations = new ArrayList<Location>();
            try {
                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject location = jsonArray.getJSONObject(i);
                    Location location1 = new Location(location);
                    locations.add(location1);
                }
                return locations;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
