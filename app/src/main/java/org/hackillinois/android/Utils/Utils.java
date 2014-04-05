package org.hackillinois.android.Utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static String loadData(URL urlToLoad) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) urlToLoad.openConnection();
            con.addRequestProperty("Email", "jacob@hackillinois.org");
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            StringWriter stringWriter = new StringWriter();
            char[] buffer = new char[8192];
            int count;
            while ((count = inputStreamReader.read(buffer, 0, buffer.length)) != -1) {
                stringWriter.write(buffer, 0, count);
            }
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
}
