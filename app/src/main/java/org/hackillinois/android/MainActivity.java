package org.hackillinois.android;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.database.PersonDatabaseLoader;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.news.NewsfeedFragment;
import org.hackillinois.android.people.PeopleDataHolder;
import org.hackillinois.android.people.PeopleSwitcherFragment;
import org.hackillinois.android.people.SearchResultsFragment;
import org.hackillinois.android.profile.ProfileFragment;
import org.hackillinois.android.rocket.RocketService;
import org.hackillinois.android.schedule.ScheduleFragment;
import org.hackillinois.android.support.SupportFragment;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String PROFILE_TAG = "profileFrag";
    private static final String PEOPLE_TAG = "peopleFrag";
    private static final String NEWS_TAG = "newsFrag";
    private static final String SCHEDULE_TAG = "scheduleFrag";
    private static final String SUPPORT_TAG = "supportFrag";
    private static final String TAG = "MainActivity";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private List<Hacker> mHackers;
    private List<Mentor> mMentorsAndStaff;
    private HashMap<String, Person> androidLookup;
    private SparseArray<Person> iOSLookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_main);
        startService(new Intent(this, RocketService.class));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Tint that shit!
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = getResources().getColor(R.color.hackillinois_blue);
            tintManager.setStatusBarTintColor(actionBarColor);
        }

        if (savedInstanceState == null) {
            new PersonDatabaseLoader(MainActivity.this).forceLoad();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the people content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){
            case 0:
                Fragment profileFragment = fragmentManager.findFragmentByTag(PROFILE_TAG);
                if (profileFragment == null) {
                    profileFragment = ProfileFragment.newInstance(position + 1);
                }
                if(fragmentManager.getBackStackEntryCount()==0)
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, profileFragment, PROFILE_TAG)
                            .commit();
                    break;
                }
                else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, profileFragment, PROFILE_TAG).addToBackStack(null)
                            .commit();
                    break;
                }
            case 1:
                Fragment peopleFragment = fragmentManager.findFragmentByTag(PEOPLE_TAG);
                if (peopleFragment == null) {
                    peopleFragment = PeopleSwitcherFragment.newInstance(position + 1);
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, peopleFragment, PEOPLE_TAG).addToBackStack(null)
                        .commit();
                break;
            case 2:
                Fragment newsFragment = fragmentManager.findFragmentByTag(NEWS_TAG);
                if (newsFragment == null) {
                    newsFragment = NewsfeedFragment.newInstance(position + 1);
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newsFragment, NEWS_TAG).addToBackStack(null)
                        .commit();
                break;
            case 3:
                Fragment scheduleFragment = fragmentManager.findFragmentByTag(SCHEDULE_TAG);
                if (scheduleFragment == null) {
                    scheduleFragment = ScheduleFragment.newInstance(position + 1);
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, scheduleFragment, SCHEDULE_TAG).addToBackStack(null)
                        .commit();
                break;
            case 4:
                Fragment supportFragment = fragmentManager.findFragmentByTag(SUPPORT_TAG);
                if (supportFragment == null) {
                    supportFragment = SupportFragment.newInstance(position + 1);
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, supportFragment, SUPPORT_TAG).addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public List<Hacker> getHackers() {
        return mHackers;
    }

    public List<Mentor> getMentorsAndStaff() {
        return mMentorsAndStaff;
    }

    public HashMap<String, Person> getAndroidLookup() {
        return androidLookup;
    }

    public SparseArray<Person> getiOSLookup() {
        return iOSLookup;
    }

    public void setPeople(PeopleDataHolder peopleDataHolder) {
        mHackers = peopleDataHolder.getHackerList();
        mMentorsAndStaff = peopleDataHolder.getMentorAndStaffList();
        androidLookup = peopleDataHolder.getAndroidMap();
        iOSLookup = peopleDataHolder.getiOSMap();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.people, menu);
            // Associate searchable configuration with the SearchView
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, RocketService.class));
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query == null) {
                finish();
            }
            showResults(query);
        }
    }

    private void showResults(String query) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchResultsFragment peopleFragment = SearchResultsFragment.newInstance(query);
        fragmentManager.beginTransaction()
                .replace(R.id.container, peopleFragment, "SEARCH_RESULTS").addToBackStack(null)
                .commit();
    }
}
