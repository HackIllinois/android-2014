package org.hackillinois.android.people;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseArray;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.AdRecord;

import java.util.HashMap;
import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NearbyFragment extends ListFragment
        implements BluetoothAdapter.LeScanCallback {

    private static final long SCAN_PERIOD = 5000;
    private static final String TAG = "NearbyFragment";
    private boolean mIsSupported;

    private BluetoothAdapter mBluetoothAdapter;
    private PeopleListAdapter mPeopleListAdapter;
    private Handler mHandler;
    private boolean mScanning;
    private SparseArray<Person> iOSLookup;
    private HashMap<String, Person> androidLookup;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        setListShown(false);
        if (!mIsSupported) {
            setEmptyText(getString(R.string.ble_not_supported));
            setListShown(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPeopleListAdapter = new PeopleListAdapter(getActivity());
        setListAdapter(mPeopleListAdapter);
        mHandler = new Handler();
        mScanning = true;
        mIsSupported = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsSupported) {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                Log.i(TAG, mBluetoothAdapter.getAddress());
                scanLeDevice(mScanning);
            }
        }

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    Log.i(TAG, "stopping scan...");
                    mBluetoothAdapter.stopLeScan(NearbyFragment.this);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            Log.i(TAG, "starting scan...");
            mBluetoothAdapter.startLeScan(NearbyFragment.this);
        } else {
            mScanning = false;
            Log.i(TAG, "stopping scan...");
            mBluetoothAdapter.stopLeScan(NearbyFragment.this);
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(TAG, "ADDRESS IS " + device.getAddress());
        List<AdRecord> adRecords = AdRecord.parseScanRecord(scanRecord);
        for (AdRecord adRecord : adRecords) {
            String record = AdRecord.getName(adRecord);
            Log.i(TAG, "record is " + record);
            int key = Integer.parseInt(record);
            Person person = iOSLookup.get(key);
            mPeopleListAdapter.add(person);
            mPeopleListAdapter.notifyDataSetChanged();
            setListShown(true);
        }
    }

    public void notifyDataReady() {
        getAndSetData();
    }

    private void getAndSetData() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            androidLookup = mainActivity.getAndroidLookup();
            iOSLookup = mainActivity.getiOSLookup();
        }
    }
}
