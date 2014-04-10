package org.hackillinois.android.people;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.hackillinois.android.R;
import org.hackillinois.android.profile.ProfileFragment;

public class ProfileViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ProfileFragment())
                    .commit();
        }
    }
}
