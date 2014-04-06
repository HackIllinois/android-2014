package org.hackillinois.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.android.Utils.Utils;
import org.hackillinois.android.models.ScheduleItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author vishal
 * <p/>
 *The top level fragment for the Schedule. Has a view pager for Friday, Saturday, and Sunday
 */
public class ScheduleFragment extends Fragment {

    private SchedulePagerAdapter mSchedulePagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.schedule_pager);
        mSchedulePagerAdapter = new SchedulePagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSchedulePagerAdapter);
        mViewPager.setClipToPadding(false);
        Utils.setInsets(getActivity(), mViewPager); // for the tinting
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
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
                    return getString(R.string.friday);
                case 1:
                    return getString(R.string.saturday);
                case 2:
                    return getString(R.string.sunday);
            }
            return null;
        }
    }

    public static class FridaySchedule extends ListFragment
            implements LoaderManager.LoaderCallbacks<List<ScheduleItem>> {

        private static final String SCHEDULE_URL = "http://www.hackillinois.org/mobile/schedule";
        private ScheduleListAdapter mListAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mListAdapter = new ScheduleListAdapter(getActivity());
            setListAdapter(mListAdapter);// set the list adapter to our custom list adapter
        }

        /**
         * Called when the fragment is visible to the user and actively running.
         * This is generally
         * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
         * Activity's lifecycle.
         */
        @Override
        public void onResume() {
            super.onResume();
            if(mListAdapter.isEmpty()){
                getLoaderManager().initLoader(0, null, this).forceLoad();
            } else {
                setListShown(true);
            }
        }

        /**
         * Called when the fragment's activity has been created and this
         * fragment's view hierarchy instantiated.  It can be used to do final
         * initialization once these pieces are in place, such as retrieving
         * views or restoring state.  It is also useful for fragments that use
         * {@link #setRetainInstance(boolean)} to retain their instance,
         * as this callback tells the fragment when it is fully associated with
         * the new activity instance.  This is called after {@link #onCreateView}
         * and before {@link #onViewStateRestored(android.os.Bundle)}.
         *
         * @param savedInstanceState If the fragment is being re-created from
         *                           a previous saved state, this is the state.
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setEmptyText("You're completely free!");
            setListShown(false);
            getListView().setBackgroundColor(getResources().getColor(R.color.background_grey));
        }

        @Override
        public void onDetach() {
            getLoaderManager().destroyLoader(0);
            super.onDetach();
        }

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<List<ScheduleItem>> onCreateLoader(int id, Bundle args) {
            try{
                return new ScheduleDataLoader(getActivity(), new URL(SCHEDULE_URL), "Friday");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Called when a previously created loader has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link android.support.v4.app.FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p/>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<List<ScheduleItem>> loader, List<ScheduleItem> data) {
            // load data into the list
            mListAdapter.setData(data);
            if(isResumed()){
                setListShown(true); // show the list
            } else {
                setListShownNoAnimation(true); // show the list but w/o animation
            }
        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<List<ScheduleItem>> loader) {

        }
    }

    public static class SaturdaySchedule extends ListFragment
            implements LoaderManager.LoaderCallbacks<List<ScheduleItem>> {

        private static final String SCHEDULE_URL = "http://www.hackillinois.org/mobile/schedule";
        private ScheduleListAdapter mListAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mListAdapter = new ScheduleListAdapter(getActivity());
            setListAdapter(mListAdapter);// set the list adapter to our custom list adapter
        }

        @Override
        public void onResume() {
            super.onResume();
            if(mListAdapter.isEmpty()){
                getLoaderManager().initLoader(0, null, this).forceLoad();
            } else {
                setListShown(true);
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setEmptyText("You're completely free!");
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
            try{
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
            if(isResumed()){
                setListShown(true); // show the list
            } else {
                setListShownNoAnimation(true); // show the list but w/o animation
            }
        }

        @Override
        public void onLoaderReset(Loader<List<ScheduleItem>> loader) {

        }
    }

    public static class SundaySchedule extends ListFragment
            implements LoaderManager.LoaderCallbacks<List<ScheduleItem>> {

        private static final String SCHEDULE_URL = "http://www.hackillinois.org/mobile/schedule";
        private ScheduleListAdapter mListAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mListAdapter = new ScheduleListAdapter(getActivity());
            setListAdapter(mListAdapter);// set the list adapter to our custom list adapter
        }

        @Override
        public void onResume() {
            super.onResume();
            if(mListAdapter.isEmpty()){
                getLoaderManager().initLoader(0, null, this).forceLoad();
            } else {
                setListShown(true);
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setEmptyText("You're completely free!");
            setListShown(false);
            getListView().setBackgroundColor(getResources().getColor(R.color.background_grey));
        }

        /**
         * Called when the fragment is no longer attached to its activity.  This
         * is called after {@link #onDestroy()}.
         */
        @Override
        public void onDetach() {
            getLoaderManager().destroyLoader(0);
            super.onDetach();
        }

        @Override
        public Loader<List<ScheduleItem>> onCreateLoader(int id, Bundle args) {
            try{
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
            if(isResumed()){
                setListShown(true); // show the list
            } else {
                setListShownNoAnimation(true); // show the list but w/o animation
            }
        }

        @Override
        public void onLoaderReset(Loader<List<ScheduleItem>> loader) {

        }
    }

}
