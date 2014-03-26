package org.hackillinois.android;

/**
 *  @author Will Hennessy
 *  An interface that can be used to listen for when the AsyncTask finishes.
 *  Use onJsonReceived(Object json) in a list fragment to access the JSON and populate UI
 */

public interface AsyncJsonListener {

    public void onJsonReceived( Object json );

}