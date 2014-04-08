package org.hackillinois.android.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;

import org.hackillinois.android.R;
import org.hackillinois.android.models.ScheduleItem;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SundaySchedule extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<ScheduleItem>> {

    private static final String SCHEDULE_URL = "http://www.hackillinois.org/mobile/schedule";
    private ScheduleListAdapter mListAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, SundaySchedule.this).forceLoad();
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ScheduleListAdapter(getActivity());
        setListAdapter(mListAdapter);// set the list adapter to our custom list adapter
        Utils.registerBroadcastReceiver(getActivity(), broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListAdapter.isEmpty()) {
            getLoaderManager().initLoader(0, null, SundaySchedule.this).forceLoad();
        } else {
            setListShown(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        setListShown(false);
        getListView().setBackgroundColor(getResources().getColor(R.color.background_grey));
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }

    @Override
    public Loader<List<ScheduleItem>> onCreateLoader(int id, Bundle args) {
        try {
            return new ScheduleDataLoader(getActivity(), new URL(SCHEDULE_URL), "Sunday");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleItem>> loader, List<ScheduleItem> data) {
        // load data into the list
        mListAdapter.setData(data);
        if (isResumed()) {
            setListShown(true); // show the list
        } else {
            setListShownNoAnimation(true); // show the list but w/o animation
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleItem>> loader) {

    }
}
