package org.hackillinois.android.people;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public class PeopleSwitcherFragment extends Fragment implements LoaderManager.LoaderCallbacks<PeopleDataHolder> {

    private static final String PEOPLE_URL = "http://hackillinois.org/mobile/person";
    private PeoplePagerAdapter mSchedulePagerAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, PeopleSwitcherFragment.this).forceLoad();
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        }
    };

    public static PeopleSwitcherFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        PeopleSwitcherFragment fragment = new PeopleSwitcherFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_login));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_switcher, container, false);
        assert rootView != null;
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.people_pager);
        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) rootView.findViewById(R.id.pager_strip);

        mSchedulePagerAdapter = new PeoplePagerAdapter(PeopleSwitcherFragment.this, getChildFragmentManager());
        mViewPager.setAdapter(mSchedulePagerAdapter);
        pagerTitleStrip.setClipToPadding(false);
        Utils.setViewPagerInsets(getActivity(), pagerTitleStrip);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<PeopleDataHolder> onCreateLoader(int id, Bundle args) {
        try {
            return new PersonDataLoader(getActivity(), new URL(PEOPLE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<PeopleDataHolder> loader, PeopleDataHolder data) {
        if (data != null) {
            ((MainActivity)getActivity()).setPeople(data);
            mSchedulePagerAdapter.notifyDataReady();
        }
    }

    @Override
    public void onLoaderReset(Loader<PeopleDataHolder> loader) {
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }

}
