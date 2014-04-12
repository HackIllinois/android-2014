package org.hackillinois.android.rocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.hackillinois.android.R;

import java.util.Random;

public class RocketService extends Service {

    private WindowManager mWindowManager;
    private LayoutInflater mLayoutInflater;

    private RelativeLayout mRelativeLayout;

    private int height;
    private int width;
    private float dpWidth;
    private Random random;

    private static final String TAG = "RocketService";

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        random = new Random();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if (Build.VERSION.SDK_INT >= 13) {
            Point point = new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            height = point.y;
            width = point.x;
        } else {
            height = mWindowManager.getDefaultDisplay().getHeight();
            width = mWindowManager.getDefaultDisplay().getWidth();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn()) {
            return Service.START_NOT_STICKY;
        }

        removeLayout();
        int startY = random.nextInt(height);
        Log.i(TAG, "startY: " + startY + " width: " + width + " height: " + height);

        mRelativeLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.rocket_layout, null);

        assert mRelativeLayout != null;
        ImageView chatHead = (ImageView) mRelativeLayout.findViewById(R.id.rock);
        chatHead.setColorFilter(getResources().getColor(R.color.hackillinois_red));
        if (random.nextInt(100) == 40) {
            chatHead.setImageResource(R.drawable.penis);
            chatHead.setColorFilter(Color.BLACK);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.y = startY;

        mWindowManager.addView(mRelativeLayout, params);
        Animation moveAnim = new TranslateAnimation(0, width + 400, 0, 0);
        moveAnim.setDuration((long) dpWidth * 3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, (moveAnim.getDuration() + 100));

        chatHead.startAnimation(moveAnim);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLayout();
    }

    private void removeLayout() {
        if (mRelativeLayout != null) {
            try {
                mWindowManager.removeView(mRelativeLayout);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
