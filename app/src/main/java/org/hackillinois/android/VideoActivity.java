package org.hackillinois.android;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by Anchal Agrawal on 3/16/14.
 */
public class VideoActivity extends Activity implements SurfaceHolder.Callback {
    private MediaPlayer mp = null;
    //...
    SurfaceView mSurfaceView=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mp = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(/*android.*/R.id.surface); // weird error here
        //...




    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + /*android.*/R.raw.launch_video);

        try {
            mp.setDataSource(this, video);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the dimensions of the video
        int videoWidth = mp.getVideoHeight();
        int videoHeight = mp.getVideoWidth();

        //Get the screen dimensions
        /*
        * Added the following lines to calculate dimensions of screen
        * */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        int screen_height = size.y;
        // added code ends above

        // now use different videos depending on screen dimensions (fix Uri parse code above)

        //Get the width of the screen
       // int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // don't need this anymore *edit anchal*

        //Get the SurfaceView layout parameters
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

        lp.width = screen_width; // added this line
        lp.height = screen_height; // added this line [FIX THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!]
        //Set the width of the SurfaceView to the width of the screen
       // lp.width = screenWidth; // don't need this anymore *edit anchal*

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0

        // don't need this anymore???????? [below]
       // lp.height = (int) (((float)videoHeight.height / (float)videoWidth.width) * (float)screen_width); // should this be videoWidth and videoHeight?

        //Commit the layout parameters
        mSurfaceView.setLayoutParams(lp);

        //Start video
        mp.setDisplay(holder); // added this fix for black screen, still doesn't work

        mp.start();

        mp.setLooping(true); //NOT SURE IF THIS SHOULD GO HERE OR NOT
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}