package org.hackillinois.android;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends Activity implements AsyncJsonListener
{
    private static final String TAG = "ProfileActivity";

    private static final String PEOPLE_URL = "http://hackillinois.org/mobile/person";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
/*
        if(savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
*/
        if(isOnline())
        {
            /* Change progress bar */
            //ProgressBar progress = (ProgressBar) findViewById(R.id.newsfeed_activity_progress);
            AsyncGetJson task = new AsyncGetJson(this, PEOPLE_URL, null);
            task.execute();
        }
        else
        {
            Toast.makeText(this, "No Internet connection found.", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onJsonReceived(Object json)
    {
        Log.d(TAG, "JSON received");
        try
        {
            JSONArray jsonArray = (JSONArray) json;
            ArrayList<PersonItem> peopleAdapterList = new ArrayList<PersonItem>();

            for(int i = 0; i < jsonArray.length(); i++)
            {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String company = jsonObject.getString("company");
                    String job_title = jsonObject.getString("job_title");
                    String location = "Need to figure this out";
                    String pictureURL = jsonObject.getString("pictureURL");
                    peopleAdapterList.add(new PersonItem(name, company,
                            job_title, location, pictureURL));
                    Log.d(TAG, jsonObject.toString());
            }
            Log.d(TAG, "Processed " + jsonArray.length() + " JSON elements");
            ListView peopleList = (ListView) findViewById(R.id.profile_activity_list);
            peopleList.setAdapter(new ProfileListAdapter(ProfileActivity.this, peopleAdapterList));
        }
        catch(JSONException e)
        {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    /** Determines if the device is online **/
    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if( netInfo != null && netInfo.isConnected() )
            return true;
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

        public PlaceholderFragment()
        {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
        }
    }

}
