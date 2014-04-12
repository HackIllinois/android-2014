package org.hackillinois.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.login.OAuthAccessFragment;

public class AuthActivity extends FragmentActivity implements LoadingInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_auth);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(getString(R.string.pref_splash_viewed), false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Tint that shit!
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = getResources().getColor(R.color.hackillinois_blue);
            tintManager.setStatusBarTintColor(actionBarColor);
        }

        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();

            OAuthAccessFragment oAuthAccessFragment = (OAuthAccessFragment) fm.findFragmentByTag("login");

            if (oAuthAccessFragment == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                oAuthAccessFragment = new OAuthAccessFragment();
                oAuthAccessFragment.setLoadingInterface(this);
                ft.replace(R.id.container, oAuthAccessFragment, null)
                .commit();
            }
        }
    }

    @Override
    public void onLoadStart() {
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadEnd() {
        setProgressBarIndeterminateVisibility(false);
    }
}
