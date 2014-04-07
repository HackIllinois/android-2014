package org.hackillinois.android.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import org.hackillinois.android.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static HttpUtils httpUtils;
    private OkHttpClient client;
    private Context mContext;

    public static HttpUtils getHttpUtils(Context context) throws IOException {
        if (httpUtils != null) {
            return httpUtils;
        }
        httpUtils = new HttpUtils(context);
        return httpUtils;
    }

    private HttpUtils(Context context) throws IOException {
        client = new OkHttpClient();
        client.setOkResponseCache(new HttpResponseCache(context.getCacheDir(), 1024));
        URL.setURLStreamHandlerFactory(client);
        mContext = context;
    }

    public String loadData(URL urlToLoad) throws IOException {
        HttpURLConnection con = client.open(urlToLoad);
        InputStream inputStream = null;
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String email = sharedPreferences.getString(mContext.getString(R.string.pref_email), "");
            con.addRequestProperty("Email", email); //"jacob@hackillinois.org");
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            inputStream = con.getInputStream();

            byte[] response = readFully(inputStream);
            return new String(response);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
