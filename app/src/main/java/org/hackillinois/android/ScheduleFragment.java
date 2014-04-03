package org.hackillinois.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author vishal
 *         <p/>
 *         The top level fragment for the Schedule. Has a view pager for Friday, Saturday, and Sunday
 */
public class ScheduleFragment extends Fragment {

    public ScheduleFragment() {
    }

    private SchedulePagerAdapter mSchedulePagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        if (rootView != null) {

            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mSchedulePagerAdapter = new SchedulePagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) getActivity().findViewById(R.id.schedule_pager);
        if (mViewPager == null) {
            Toast.makeText(getActivity(), "FUCKKK", Toast.LENGTH_SHORT).show();
        } else
            mViewPager.setAdapter(mSchedulePagerAdapter);
    }

    public class SchedulePagerAdapter extends FragmentPagerAdapter {

        public SchedulePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position position
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FridaySchedule();
                case 1:
                    return new SaturdaySchedule();
                case 2:
                    return new SundaySchedule();
            }
            return null;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * This method may be called by the ViewPager to obtain a title string
         * to describe the specified page. This method may return null
         * indicating no title for this page. The default implementation returns
         * null.
         *
         * @param position The position of the title requested
         * @return A title for the requested page
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Friday";
                case 1:
                    return "Saturday";
                case 2:
                    return "Sunday";
            }
            return null;
        }
    }

    public static class FridaySchedule extends Fragment {

        public FridaySchedule() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public static class SaturdaySchedule extends Fragment {

        public SaturdaySchedule() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public static class SundaySchedule extends Fragment {

        public SundaySchedule() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

}
