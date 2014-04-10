package org.hackillinois.android.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.utils.Utils;

/**
 * @author vishal
 *         <p/>
 * The top level fragment for the Schedule. Has a view pager for Friday, Saturday, and Sunday
 */
public class ScheduleFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.schedule_pager);
        SchedulePagerAdapter mSchedulePagerAdapter = new SchedulePagerAdapter(this, getChildFragmentManager());
        mViewPager.setAdapter(mSchedulePagerAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setClipToPadding(false);
        Utils.setInsets(getActivity(), mViewPager);
        return rootView;
    }

    public static ScheduleFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));
    }

}
