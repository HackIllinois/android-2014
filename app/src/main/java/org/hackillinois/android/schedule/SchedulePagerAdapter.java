package org.hackillinois.android.schedule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.hackillinois.android.R;

/**
* Created by Vishal on 4/8/14.
*/
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    private ScheduleFragment scheduleFragment;

    public SchedulePagerAdapter(ScheduleFragment scheduleFragment, FragmentManager fm) {
        super(fm);
        this.scheduleFragment = scheduleFragment;
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
                return scheduleFragment.getString(R.string.friday);
            case 1:
                return scheduleFragment.getString(R.string.saturday);
            case 2:
                return scheduleFragment.getString(R.string.sunday);
        }
        return null;
    }
}
