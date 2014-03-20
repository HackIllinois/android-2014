package org.hackillinois.android;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by fishbeinb on 3/19/14.
 */
public class HelpActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.hide();
        }

    }

}
