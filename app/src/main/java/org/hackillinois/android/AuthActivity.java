package org.hackillinois.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.hackillinois.android.login.OAuthAccessFragment;

public class AuthActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String email = sharedPreferences.getString(getString(R.string.pref_email), "");
            final FragmentManager fm = getSupportFragmentManager();

            OAuthAccessFragment oAuthAccessFragment = (OAuthAccessFragment) fm.findFragmentByTag("login");

            if (email.length() == 0 && oAuthAccessFragment == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                oAuthAccessFragment = new OAuthAccessFragment();
                oAuthAccessFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_DarkActionBar);
                oAuthAccessFragment.setCancelable(false);
                oAuthAccessFragment.show(ft, "login");
            }
        }
    }
}
