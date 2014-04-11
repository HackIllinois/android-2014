package org.hackillinois.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import org.hackillinois.android.R;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static HttpUtils httpUtils;
    private OkHttpClient client;
    private Context mContext;

    private static final String EMAIL_URL = "http://www.hackillinois.org/mobile/login";
    private static final String POST_URL = "http://www.hackillinois.org/mobile/person";

    public static synchronized HttpUtils getHttpUtils(Context context) throws IOException {
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
            con.addRequestProperty("Email", email);
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


    public String postPersonData(String key, String body, String type) throws IOException{
        URL url = new URL(POST_URL);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String email = sharedPreferences.getString(mContext.getString(R.string.pref_email), "");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");

        con.addRequestProperty("Email", email);
        con.setRequestProperty("Content-Type", "text/json");
        con.setRequestProperty("charset", "utf-8");

        String content = "{\"" + key + "\":" + body + "}";
        Log.e("content", content);
        con.setRequestProperty("Content-Length", Integer.toString(content.getBytes().length));

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(content);
        wr.flush();
        wr.close();
        DataInputStream  inputStream = new DataInputStream(con.getInputStream());
        byte[] response = readFully(inputStream);
        String responseString = new String(response);
        Log.i("HttpUtils", "response was " + responseString);
        con.disconnect();
        if(response.equals("{\"message\": \"Updated Profile\"}"))
            return "Post Success";
        return "Post Fail";
    }
    public String testEmail(String email) throws IOException {
        HttpURLConnection con = client.open(new URL(EMAIL_URL));
        InputStream inputStream = null;
        try {
            con.addRequestProperty("Email", email);
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
