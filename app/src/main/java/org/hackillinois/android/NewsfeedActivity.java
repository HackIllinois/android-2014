package org.hackillinois.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.hackillinois.android.models.NewsItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsfeedActivity extends Activity implements AsyncJsonListener {

    private static final String NEWSFEED_JSON_URL = "http://www.hackillinois.org/mobile/newsfeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        loadNews();
    }


    private void loadNews() {
        if( isOnline() ) {
            ProgressBar progress = (ProgressBar) findViewById(R.id.newsfeed_activity_progress);
            AsyncGetJson task = new AsyncGetJson(this, NEWSFEED_JSON_URL, progress);
            task.execute();
        } else {
            Toast.makeText(this, "No Internet connection found.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.newsfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newsfeed_menu_refresh) {
            loadNews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Determine if the device is online **/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if( netInfo != null && netInfo.isConnected() )
            return true;
        return false;
    }

    /** Callback method that is called after the JSON is received **/
    @Override
    public void onJsonReceived(Object json) {
        try {
            /** Get the data out of the JSON. The exact get("*")'s here will depend on what
             *  the backend returns.  {} is a JSONObject, and [] is a JSONArray */
            JSONArray jsonArray = (JSONArray) json;
            ArrayList<NewsItem> allNewsItems = new ArrayList<NewsItem>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject curr = jsonArray.getJSONObject(i);
                String description = curr.getString("description");
                int time = curr.getInt("time");
                String iconUrl = curr.getString("icon_url");
                boolean isEmergency = curr.getBoolean("emergency");

                NewsItem buildingItem = new NewsItem(description, time, iconUrl, isEmergency);

                JSONArray jsonHighlights = curr.getJSONArray("highlighted");
                for( int j = 0; j < jsonHighlights.length(); j++ ) {
                    JSONArray currHighlight = jsonHighlights.getJSONArray(j);

                    // range
                    JSONArray range = currHighlight.getJSONArray(0);
                    int startIdx = range.getInt(0);
                    int endIdx = range.getInt(1);

                    // color
                    JSONArray color = currHighlight.getJSONArray(1);
                    int red = color.getInt(0);
                    int green = color.getInt(1);
                    int blue = color.getInt(2);

                    buildingItem.addHighlight(startIdx, endIdx, red, green, blue);
                }

                allNewsItems.add( buildingItem );
            }

            ListView newsfeedList = (ListView) findViewById(R.id.newsfeed_activity_list);
            newsfeedList.setAdapter(new NewsfeedListAdapter(NewsfeedActivity.this, allNewsItems));


        } catch(JSONException j) {
            j.printStackTrace();
            Toast.makeText(this, "Error in retrieving data. Please try again.", Toast.LENGTH_LONG).show();
        } catch(NullPointerException n) {
            n.printStackTrace();
            Toast.makeText(this, "Error in retrieving data. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

}
