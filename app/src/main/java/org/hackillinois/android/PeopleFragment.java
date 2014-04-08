package org.hackillinois.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;

import org.hackillinois.android.Utils.Utils;
import org.hackillinois.android.models.people.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PeopleFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Person>> {

    private static final String PEOPLE_URL = "http://hackillinois.org/mobile/person";

    private PeopleListAdapter mPeopleListAdapter;
    private OnDataPass dataPasser;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, PeopleFragment.this).forceLoad();
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        setListShown(false);
        getListView().setClipToPadding(false);
        Utils.setInsets(getActivity(), getListView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPeopleListAdapter = new PeopleListAdapter(getActivity());
        setListAdapter(mPeopleListAdapter);
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_login));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPeopleListAdapter.isEmpty()) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            setListShown(true);
        }
        ((MainActivity) getActivity()).onSectionAttached(1);
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
        mPeopleListAdapter.setData(data);
        dataPasser.onDataPass(data);

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataPasser = (OnDataPass) activity;
    }

    public interface OnDataPass {
        public void onDataPass(final List<Person> people);
    }
}
