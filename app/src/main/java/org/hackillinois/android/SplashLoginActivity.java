package org.hackillinois.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;


public class SplashLoginActivity extends Activity {

    private ImageView splash;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dont_launch_this_activity_but_launch_the_main_activity = sharedPreferences.getBoolean(
                getString(R.string.pref_splash_viewed), false
        );
        if (dont_launch_this_activity_but_launch_the_main_activity) {
            startActivity(new Intent(SplashLoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_splash_login);
        splash = (ImageView) findViewById(R.id.splash_image_activity);
        final ImageView button = (ImageView) findViewById(R.id.launchbutton_activity);
        video = (VideoView) findViewById(R.id.videoView_activity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SplashLoginActivity.this, AuthActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });

        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.launch_video));

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splash.setVisibility(View.INVISIBLE);
                    }
                }, 200);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


    }

}
