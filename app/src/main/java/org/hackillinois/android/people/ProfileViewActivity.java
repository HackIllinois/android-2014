package org.hackillinois.android.people;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.hackillinois.android.LoadingInterface;
import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
import org.hackillinois.android.profile.ProfileFragment;

public class ProfileViewActivity extends ActionBarActivity implements LoadingInterface{

    String email;

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
                assert person != null;
                email = person.getEmail();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.send_email == item.getItemId()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.setType("message/rfc822");
            startActivityForResult(intent, 5);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
