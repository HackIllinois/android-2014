package org.hackillinois.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NewsfeedActivity extends Activity implements AsyncJsonListener {

    private static final String NEWSFEED_JSON_URL = "http://dev.hackillinois.org/mobile/newsfeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Determines if the device is online **/
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
            String output = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject curr = jsonArray.getJSONObject(i);
                String description = curr.getString("description");
                output += description + "\n";
            }

            TextView newsfeedText = (TextView) findViewById(R.id.newsfeed_activity_text);
            newsfeedText.setText( output );

        } catch(JSONException j) {
            j.printStackTrace();
            Toast.makeText(this, "Error in retrieving data. Please try again.", Toast.LENGTH_LONG).show();
        } catch(NullPointerException n) {
            n.printStackTrace();
            Toast.makeText(this, "Error in retrieving data. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
