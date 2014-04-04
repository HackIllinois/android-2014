package org.hackillinois.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import org.hackillinois.android.Utils.AdRecord;
import org.hackillinois.android.models.people.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NearbyFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Person>>,
        BluetoothAdapter.LeScanCallback {

    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = "NearbyFragment";
    private static final String PEOPLE_URL = "http://hackillinois.org/mobile/person";

    private BluetoothAdapter mBluetoothAdapter;
    private NearbyListAdapter mNearbyListAdapter;
    private Handler mHandler;
    private boolean mScanning;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No peeps");
        setListShown(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNearbyListAdapter = new NearbyListAdapter(getActivity());
        setListAdapter(mNearbyListAdapter);
        mHandler = new Handler();
        mScanning = false;
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            setEmptyText(getString(R.string.ble_not_supported));
            setListShown(true);
        } else {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            Log.i(TAG, mBluetoothAdapter.getAddress());
            scanLeDevice(mScanning);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNearbyListAdapter.isEmpty()) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            setListShown(true);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(NearbyFragment.this);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(NearbyFragment.this);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(NearbyFragment.this);
        }
    }

    @Override
    public Loader<List<Person>> onCreateLoader(int id, Bundle args) {
        try {
            return new PersonDataLoader(getActivity(), new URL(PEOPLE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Person>> loader, List<Person> data) {
        mNearbyListAdapter.setData(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Person>> loader) {
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(TAG, "ADDRESS IS " + device.getAddress());
        List<AdRecord> adRecords = AdRecord.parseScanRecord(scanRecord);
        for (AdRecord adRecord : adRecords) {
            Log.i(TAG, AdRecord.getName(adRecord));
        }
    }

}
