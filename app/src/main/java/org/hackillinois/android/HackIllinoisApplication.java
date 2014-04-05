package org.hackillinois.android;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import java.net.URL;

public class HackIllinoisApplication extends Application{

    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        URL.setURLStreamHandlerFactory(okHttpClient);
    }
}
