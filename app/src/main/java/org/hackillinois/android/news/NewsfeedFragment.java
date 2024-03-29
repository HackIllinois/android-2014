package org.hackillinois.android.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.NewsItem;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsfeedFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<NewsItem>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String NEWSFEED_JSON_URL = "http://www.hackillinois.org/mobile/newsfeed";
    private NewsfeedListAdapter mNewsfeedListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mListShown = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, NewsfeedFragment.this).forceLoad();
                mSwipeRefreshLayout.setRefreshing(true);
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this);
            }
        }
    };

    public static NewsfeedFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        NewsfeedFragment fragment = new NewsfeedFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = getListView();
        list.setBackgroundColor(getResources().getColor(R.color.background_grey));
        list.setDividerHeight(0);
        setListShown(false);
        list.setClipToPadding(false);
        Utils.setInsetsBottom(getActivity(), list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, null, false);
        Utils.setViewPagerInsets(getActivity(), v);
        assert v != null;
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(
                R.color.hackillinois_red,
                R.color.hackillinois_blue_trans,
                R.color.hackillinois_blue,
                R.color.hackillinois_red_trans
        );
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsfeedListAdapter = new NewsfeedListAdapter(getActivity());
        setListAdapter(mNewsfeedListAdapter);
        Utils.registerBroadcastReceiver(getActivity(), broadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNewsfeedListAdapter.isEmpty()) {
            mSwipeRefreshLayout.setRefreshing(true);
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
        mSwipeRefreshLayout.setRefreshing(false);
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
    public void onRefresh() {
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    public void setListShown(boolean shown, boolean animate){
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mSwipeRefreshLayout.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            }
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mSwipeRefreshLayout.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            }
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        }
    }
    public void setListShown(boolean shown){
        setListShown(shown, true);
    }
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }
}

