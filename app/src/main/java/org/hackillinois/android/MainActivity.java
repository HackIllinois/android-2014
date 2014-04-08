package org.hackillinois.android;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.login.OAuthAccessFragment;
import org.hackillinois.android.models.people.Person;

import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        PeopleFragment.OnDataPass {

    private static final String PEOPLE_TAG = "peopleFrag";
    private static final String NEWS_TAG = "newsFrag";
    private static final String SCHEDULE_TAG = "scheduleFrag";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private List<Person> mPeople;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final FragmentManager fm = getSupportFragmentManager();
        SplashScreenDialogFragment splashFragment = (SplashScreenDialogFragment) fm.findFragmentByTag("splash");
        OAuthAccessFragment oAuthAccessFragment = (OAuthAccessFragment) fm.findFragmentByTag("login");
        boolean viewed = sharedPreferences.getBoolean(getString(R.string.pref_splash_viewed), false);
        String email = sharedPreferences.getString(getString(R.string.pref_email), "");

        if (email.length() == 0 && oAuthAccessFragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            oAuthAccessFragment = new OAuthAccessFragment();
            oAuthAccessFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_DarkActionBar);
            oAuthAccessFragment.show(ft, "login");
        }

        if (!viewed && splashFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            splashFragment = new SplashScreenDialogFragment();
            splashFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Hackillinois_Launcher);
            splashFragment.show(fragmentTransaction, "splash");
        }

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(getResources().getDrawable(R.drawable.hackillinois_icon_white));

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
            int actionBarColor = getResources().getColor(R.color.hackillinois_blue_trans);
            tintManager.setStatusBarTintColor(actionBarColor);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){
            case 0:
                Fragment peopleFragment = fragmentManager.findFragmentByTag(PEOPLE_TAG);
                if (peopleFragment == null) {
                    peopleFragment = new PeopleFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, peopleFragment, PEOPLE_TAG).addToBackStack(null)
                        .commit();
                break;
            case 1:
                Fragment newsFragment = fragmentManager.findFragmentByTag(NEWS_TAG);
                if (newsFragment == null) {
                    newsFragment = new NewsfeedFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newsFragment, NEWS_TAG).addToBackStack(null)
                        .commit();
                break;
            case 2:
                Fragment scheduleFragment = fragmentManager.findFragmentByTag(SCHEDULE_TAG);
                if (scheduleFragment == null) {
                    scheduleFragment = new ScheduleFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container, scheduleFragment, SCHEDULE_TAG).addToBackStack(null)
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
        }
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
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataPass(final List<Person> people) {
        mPeople = people;
    }

}
