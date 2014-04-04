package org.hackillinois.android;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Stephen Herring on 3/31/14.
 */
public class SkillsActivity extends Activity
{
    private static final String SKILL_URL = "http://hackillinois.org/mobile/skills";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);

    }
}
