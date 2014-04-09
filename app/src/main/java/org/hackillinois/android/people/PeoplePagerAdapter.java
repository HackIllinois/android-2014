package org.hackillinois.android.people;

import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.hackillinois.android.R;

public class PeoplePagerAdapter extends FragmentPagerAdapter {

    private PeopleSwitcherFragment mPeopleSwitcherFragment;
    private HackersFragment mHackersFragment;
    private NearbyFragment mNearbyFragment;

    public PeoplePagerAdapter(PeopleSwitcherFragment peopleSwitcherFragment, FragmentManager fm) {
        super(fm);
        this.mPeopleSwitcherFragment = peopleSwitcherFragment;
        mHackersFragment = new HackersFragment();
        mNearbyFragment = new NearbyFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mHackersFragment;
            case 1:
                return new HackersFragment();
            case 2:
                return mNearbyFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mPeopleSwitcherFragment.getString(R.string.hackers_tab);
            case 1:
                return mPeopleSwitcherFragment.getString(R.string.mentors_tab);
            case 2:
                return mPeopleSwitcherFragment.getString(R.string.nearby_tab);
        }
        return null;
    }

    public void notifyDataReady() {
        mHackersFragment.notifyDataReady();
        mNearbyFragment.notifyDataReady();
    }

    public void showResults(Cursor c, String query, int currentPage) {
        Fragment fragment = mPeopleSwitcherFragment.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.people_pager + ":" + currentPage);
        if (fragment instanceof HackersFragment) {
            ((HackersFragment) fragment).showResults(c, query);
        }
    }
}
