package org.hackillinois.android.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.ScheduleItem;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SaturdaySchedule extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<ScheduleItem>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String SCHEDULE_URL = "http://www.hackillinois.org/mobile/schedule";
    private ScheduleListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mListShown = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, SaturdaySchedule.this).forceLoad();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saturday, container, false);
        assert rootView != null;
        View list = rootView.findViewById(android.R.id.list);
        Utils.setInsetsBottom(getActivity(), list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.saturday_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(
                R.color.hackillinois_red,
                R.color.hackillinois_blue_trans,
                R.color.hackillinois_red_trans,
                R.color.hackillinois_blue

        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListAdapter.isEmpty()) {
            mSwipeRefreshLayout.setRefreshing(true);
            getLoaderManager().initLoader(0, null, SaturdaySchedule.this).forceLoad();

        } else {
            setListShown(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // setEmptyText(getString(R.string.loading_data_error));
        setListShown(false);
        getListView().setBackgroundColor(getResources().getColor(R.color.background_grey_trans));
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }

    @Override
    public Loader<List<ScheduleItem>> onCreateLoader(int id, Bundle args) {
        try {
            return new ScheduleDataLoader(getActivity(), new URL(SCHEDULE_URL), "Saturday");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleItem>> loader, List<ScheduleItem> data) {
        // load data into the list
        mListAdapter.setData(data);
        mSwipeRefreshLayout.setRefreshing(false);
        if (isResumed()) {
            setListShown(true); // show the list
        } else {
            setListShownNoAnimation(true); // show the list but w/o animation
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleItem>> loader) {

    }

    @Override
    public void onRefresh() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ImageView view = (ImageView) getActivity().findViewById(R.id.rocketship);
            //ViewPropertyAnimator animate = view.animate();
//            if (animate != null) {
//                animate.rotationBy(360f);
//            }
        }
        getLoaderManager().initLoader(0, null, SaturdaySchedule.this).forceLoad();
    }

    @Override
    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    @Override
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    public void setListShown(boolean shown, boolean animate){
        if(mListShown == shown){
            return; //the list is already shown
        }

        mListShown = shown;

        if(shown){
//            if(animate){
//                mSwipeRefreshLayout.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_in));
//            }
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        else{
//            if(animate){
//                mSwipeRefreshLayout.startAnimation(AnimationUtils.loadAnimation(
//                        getActivity(), android.R.anim.fade_out));
//            }
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        }
    }
}
