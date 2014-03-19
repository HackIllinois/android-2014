package org.hackillinois.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by fishbeinb on 3/19/14.
 */
public class TempNavActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_auth);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.hide();
        }

        final Button GoogleAuthButton = (Button) findViewById(R.id.GoogleAuthNavBtn);
        GoogleAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TempNavActivity.this, "Google Auth", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TempNavActivity.this, GoogleAuthActivity.class));
            }
        });

        final Button HelpButton = (Button) findViewById(R.id.HelpNavBtn);
        GoogleAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TempNavActivity.this, "Help", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TempNavActivity.this, HelpActivity.class));
            }
        });
    }

}
