package org.hackillinois.android;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * @author Vishal Disawar, Will Hennessy
 *         SplashScreenDialogFragment -- rocket ship VideoView in the background, with the I logo
 *         and Launch button overlayed.
 */

public class SplashScreenDialogFragment extends DialogFragment {

    private ImageView splash;
    private VideoView video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        splash = (ImageView) rootView.findViewById(R.id.splash_image);
        final ImageView button = (ImageView) rootView.findViewById(R.id.launchbutton);
        video = (VideoView) rootView.findViewById(R.id.videoView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //video.stopPlayback();
                //video.setVisibility(View.INVISIBLE);
//                splash.setVisibility(View.VISIBLE);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.pref_splash_viewed), true);
                editor.commit();
                dismiss();
            }
        });

        video.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.launch_video));

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        video.stopPlayback();
        splash.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
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
                }, 100);

            }
        });
    }
}

