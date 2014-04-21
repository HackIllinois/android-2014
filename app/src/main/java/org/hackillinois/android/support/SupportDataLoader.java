package org.hackillinois.android.support;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.models.Support;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SupportDataLoader extends AsyncTaskLoader<Support> {

    private URL url;
    private Context mContext;

    public SupportDataLoader(Context context) throws MalformedURLException {
        super(context);
        mContext = context;
        this.url = new URL("http://www.hackillinois.org/mobile/support");
    }

    @Override
    public Support loadInBackground() {
        String data = null;
        try{
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null){
            Support categories = new Support();
            try {
                JSONObject jsonobject = new JSONObject(data.toString());
                Iterator Categ = jsonobject.keys();
                while(Categ.hasNext()) {
                    String category = Categ.next().toString();
                    List<String> subCategoryList = new ArrayList<String>();
                    JSONArray subCategoryArray = jsonobject.getJSONArray(category);
                    for (int i = 0; i < subCategoryArray.length(); i++) {
                        subCategoryList.add(subCategoryArray.getString(i));
                    }
                    categories.addCategory(category, subCategoryList);
                }
                return categories;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

