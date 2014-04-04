package org.hackillinois.android.Utils;

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
}
