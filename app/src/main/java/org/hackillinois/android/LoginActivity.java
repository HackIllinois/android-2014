package org.hackillinois.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * @author Vishal Disawar, Will Hennessy
 *         LoginActivity -- rocket ship VideoView in the background, with the I logo
 *         and Launch button overlayed.
 */

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.hide();
        }

        final ImageView button = (ImageView) findViewById(R.id.launchbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, TempNavActivity.class));//GoogleAuthActivity.class));
            }
        });

        final VideoView video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.launch_video));
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.requestFocus();
                video.start();
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
    }

}

