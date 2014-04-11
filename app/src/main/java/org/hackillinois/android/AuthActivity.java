package org.hackillinois.android;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.login.OAuthAccessFragment;

public class AuthActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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
                ft.replace(R.id.container, oAuthAccessFragment, null)
                .commit();
            }
        }
    }
}
