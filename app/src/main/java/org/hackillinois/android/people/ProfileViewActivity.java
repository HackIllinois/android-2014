package org.hackillinois.android.people;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.hackillinois.android.profile.ProfileFragment;

public class ProfileViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = getResources().getColor(R.color.hackillinois_red);
            tintManager.setStatusBarTintColor(actionBarColor);
        }

        if (savedInstanceState == null) {
            if (getIntent().getExtras() != null) {
                Person person = (Person) getIntent().getExtras().getSerializable("person");
                if (person instanceof Hacker) {
                    getSupportActionBar().setTitle(getString(R.string.hacker));
                } else if (person instanceof Staff) {
                    getSupportActionBar().setTitle(getString(R.string.staff));
                } else if (person instanceof Mentor) {
                    getSupportActionBar().setTitle(getString(R.string.mentor));
                }
                ProfileFragment profileFragment = ProfileFragment.newViewInstance(person);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, profileFragment)
                        .commit();
            }
        }
    }
}
