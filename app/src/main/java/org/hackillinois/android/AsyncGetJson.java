package org.hackillinois.android;

/**
 * @author Will Hennessy
 *
 * An AsyncTask class that fetches a JSON from a URL.
 * Runs on a separate thread, not the UI thread.
 *
 * An activity may implement AsyncJsonListener, then execute this task, and this task
 * will call the onJsonReceived callback method after the JSON is retrieved.
 */

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AsyncGetJson extends AsyncTask<String, Void, Integer> {

    private static final int JSON_SUCCESS = 0x0;
    private static final int JSON_FAIL = 0x1;

    private String json_url;			// the url of the JSON
    private AsyncJsonListener callback; // a reference to the AsyncJsonListener that requested this JSON
    private ProgressBar progress;		// the progress bar provided by the AsyncJsonListener
    private Object json;


    /** Constructor
     * @param callback - a class that implement AsyncJsonListener
     * @param json_url - the URL of the JSON to fetch
     * @param progress - (optional) the progress bar in the view that is loading JSON
    **/
    public AsyncGetJson(AsyncJsonListener callback, String json_url, ProgressBar progress) {
        this.callback = callback;
        this.json_url = json_url;
        this.progress = progress;
    }


    @Override
    protected void onPreExecute() {
        if(progress != null)
            progress.setVisibility(View.VISIBLE);
    }


    @Override
    protected Integer doInBackground(String... arg0) {

        try {
            URL source = new URL(json_url);

            /** get the JSON string **/
            BufferedReader in = new BufferedReader( new InputStreamReader( source.openStream() ) );
            String json_str = "";
            String line;
            while( (line = in.readLine()) != null )	// get the JSON
                json_str += line;
            in.close();


            /** Must be able to retrieve either a JSONObject or JSONArray **/
            if(json_str.charAt(0) == '{')
                json = new JSONObject( json_str );
            else if(json_str.charAt(0) == '[')
                json = new JSONArray( json_str );
        }
        catch(Exception e) {
            e.printStackTrace();
            return JSON_FAIL;
        }

        return JSON_SUCCESS;
    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        /** call the listener that will populate the UI. Give it the JSON **/
        if(result == JSON_FAIL)
            json = null;

        if(progress != null)
            progress.setVisibility(View.GONE);

        callback.onJsonReceived(json);	// pass the JSON array to the list that called this AsyncTask
    }

}