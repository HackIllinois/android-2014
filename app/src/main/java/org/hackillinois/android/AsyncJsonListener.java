package org.hackillinois.android;

import org.json.JSONObject;

/**
 *  @author Will Hennessy
 *  An interface that can be used to listen for when the AsyncTask finishes.
 *  Use onJsonReceived(JSONObject json) in a list fragment to access the JSON and populate UI
 */

public interface AsyncJsonListener {

    public void onJsonReceived( JSONObject json );

}