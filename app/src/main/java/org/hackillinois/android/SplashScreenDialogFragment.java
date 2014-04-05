package org.hackillinois.android;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login, container, false);
        final ImageView splash = (ImageView) rootView.findViewById(R.id.splash_image);
        final ImageView button = (ImageView) rootView.findViewById(R.id.launchbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splash.setVisibility(View.VISIBLE);
                dismiss();
            }
        });

        final VideoView video = (VideoView) rootView.findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.launch_video));
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
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
        return rootView;
    }
}

