package org.hackillinois.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Stephen Herring on 3/31/14.
 */
public class SkillsActivity extends Activity implements AsyncJsonListener
{
    private static final String SKILL_URL = "http://hackillinois.org/mobile/skills";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        Log.e("HELLO", "dawggy");

        /* Might want to change to a custom ProgressBar */
        AsyncGetJson getter = new AsyncGetJson(this, SKILL_URL, null);
    }

    @Override
    public void onJsonReceived(Object json)
    {

    }
}
