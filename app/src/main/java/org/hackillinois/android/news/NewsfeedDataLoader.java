package org.hackillinois.android.news;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.models.NewsItem;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsfeedDataLoader extends AsyncTaskLoader<List<NewsItem>> {

    private URL urlToLoad;
    private Context mContext;

    public NewsfeedDataLoader(Context context, URL url) {
        super(context);
        mContext = context;
        urlToLoad = url;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        String data = null;
        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(urlToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {

            try {
                JSONArray jsonArray = new JSONArray(data);
                ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
                ArrayList<NewsItem> emergencyNewsItems = new ArrayList<NewsItem>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject curr = jsonArray.getJSONObject(i);
                    String description = curr.getString("description");
                    int time = curr.getInt("time");
                    String iconUrl = curr.getString("icon_url");
                    boolean isEmergency = curr.getBoolean("emergency");

                    NewsItem buildingItem = new NewsItem(description, time, iconUrl, isEmergency);

                    JSONArray jsonHighlights = curr.getJSONArray("highlighted");
                    for( int j = 0; j < jsonHighlights.length(); j++ ) {
                        JSONArray currHighlight = jsonHighlights.getJSONArray(j);

                        // range
                        JSONArray range = currHighlight.getJSONArray(0);
                        int startIdx = range.getInt(0);
                        int endIdx = range.getInt(1);

                        // color
                        JSONArray color = currHighlight.getJSONArray(1);
                        int red = color.getInt(0);
                        int green = color.getInt(1);
                        int blue = color.getInt(2);

                        buildingItem.addHighlight(startIdx, endIdx, red, green, blue);
                    }

                    if(isEmergency)
                        emergencyNewsItems.add(0, buildingItem);
                    else
                        newsItems.add(0, buildingItem );
                }

                // put the emergency items at the top, followed by the others news items
                emergencyNewsItems.addAll(newsItems);

                return emergencyNewsItems;

            } catch(JSONException j) {
                j.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
