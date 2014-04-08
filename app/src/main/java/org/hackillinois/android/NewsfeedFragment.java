package org.hackillinois.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import org.hackillinois.android.Utils.Utils;
import org.hackillinois.android.models.NewsItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsfeedFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private static final String NEWSFEED_JSON_URL = "http://www.hackillinois.org/mobile/newsfeed";
    private NewsfeedListAdapter mNewsfeedListAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, NewsfeedFragment.this).forceLoad();
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this);
            }
        }
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        ListView list = getListView();
        list.setBackgroundColor(getResources().getColor(R.color.background_grey));
        list.setDividerHeight(0);
        setListShown(false);
        list.setClipToPadding(false);
        Utils.setInsets(getActivity(), list);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsfeedListAdapter = new NewsfeedListAdapter(getActivity());
        setListAdapter(mNewsfeedListAdapter);
        Utils.registerBroadcastReceiver(getActivity(), broadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNewsfeedListAdapter.isEmpty()) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            setListShown(true);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        try {
            return new NewsfeedDataLoader(getActivity(), new URL(NEWSFEED_JSON_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        mNewsfeedListAdapter.setData(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

}
