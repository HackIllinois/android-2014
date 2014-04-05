package org.hackillinois.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by fishbeinb on 3/19/14.
 *
 * This class is temporary-- it is not part of the real app.
 * It is only used to bypass google authentication for testing purposes.
 * -- Will
 */
public class TempNavActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tempnav);

        final Button GoogleAuthButton = (Button) findViewById(R.id.GoogleAuthNavBtn);
        GoogleAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TempNavActivity.this, GoogleAuthActivity.class));
            }
        });

        final Button HelpButton = (Button) findViewById(R.id.HelpNavBtn);
        HelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TempNavActivity.this, HelpActivity.class));
            }
        });

        final Button NewsfeedButton = (Button) findViewById(R.id.NewsfeedBtn);
        NewsfeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TempNavActivity.this, NewsfeedActivity.class));
            }
        });

        final Button SkillsButton = (Button) findViewById(R.id.SkillsBtn);
        SkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TempNavActivity.this, SkillsActivity.class));
            }
        });


        }

}
