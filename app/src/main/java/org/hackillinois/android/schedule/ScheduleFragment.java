package org.hackillinois.android.schedule;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.utils.Utils;

/**
 * @author vishal
 *         <p/>
 *         The top level fragment for the Schedule. Has a view pager for Friday, Saturday, and Sunday
 */
public class ScheduleFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private int mCurrentPosition;//Updated before animation begins
    private int mScrollState;
    private int mScreenWidth;
    private static boolean rotated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= 13) {
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            mScreenWidth = point.x;
        } else {
            mScreenWidth = wm.getDefaultDisplay().getWidth();
        }

        rotated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.schedule_pager);
        SchedulePagerAdapter mSchedulePagerAdapter = new SchedulePagerAdapter(this, getChildFragmentManager());
        mViewPager.setAdapter(mSchedulePagerAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setOnPageChangeListener(this);

        PagerTitleStrip mStrip = (PagerTitleStrip) rootView.findViewById(R.id.schedule_pager_title_strip);

        mViewPager.setClipToPadding(false);
        Utils.setViewPagerInsets(getActivity(), rootView);
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

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (Build.VERSION.SDK_INT >= 14) {
            if (positionOffset <= 0 || positionOffset >= 1)
                return;

            ImageView view = (ImageView) getActivity().findViewById(R.id.rocketship);
            ViewPropertyAnimator animate = view.animate();
            if (animate != null && mScrollState == ViewPager.SCROLL_STATE_SETTLING) {
                if (mCurrentPosition == (position + 1)){ // move to the right
                    if (rotated) {
                        animate.rotation(0f);
                        rotated = false;
                    }
                    if (position == 0)
                        animate.x(0.45f * mScreenWidth);
                    if (position == 1)
                        animate.x(.8f * mScreenWidth);

                    if (mScrollState == ViewPager.SCROLL_STATE_IDLE)
                        animate.cancel();
                } else if (mCurrentPosition == position) { // move to the left
                    if (!rotated) {
                        animate.rotation(180f);
                        rotated = true;
                    }
                    if (position == 0)
                        animate.x(1f/15f * mScreenWidth);
                    if (position == 1)
                        animate.x(4f/9f * mScreenWidth);

                    if (mScrollState == ViewPager.SCROLL_STATE_IDLE)
                        animate.cancel();
                }
            }
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see android.support.v4.view.ViewPager#SCROLL_STATE_IDLE
     * @see android.support.v4.view.ViewPager#SCROLL_STATE_DRAGGING
     * @see android.support.v4.view.ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
    }

}
