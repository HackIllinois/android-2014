package org.hackillinois.android.support;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.hackillinois.android.models.Support.Support;
import org.hackillinois.android.models.Support.SupportData;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SupportDataLoader extends AsyncTaskLoader<SupportData> {

    private URL urlRooms;
    private URL urlCategories;
    private Context mContext;

    public SupportDataLoader(Context context, URL urlRooms, URL urlCategories) {
        super(context);
        mContext = context;
        urlRooms = urlRooms;
        urlCategories = urlCategories;
    }

    @Override
    public SupportData loadInBackground() {

        Log.e("FA","Fa213sd");
        String roomData = null;
        String categoryData = null;

        List<Support> roomList = new ArrayList<Support>();
        List<Support> categoryList = new ArrayList<Support>();
        Map<Support, List<Support>> subCategoriesMap = new HashMap<Support, List<Support>>();

        try {
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            roomData = httpUtils.loadData(new URL("http://www.hackillinois.org/mobile/map"));
            categoryData = httpUtils.loadData(new URL("http://www.hackillinois.org/mobile/support"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.e("FA","Fa312sd");
        if (roomData != null) {
            try {
                JSONArray jsonArray = new JSONArray(roomData);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject curr = jsonArray.getJSONObject(i);
                    String roomNumber = curr.getString("room_number");
                    Log.e("rf", roomNumber);
                    Support room = new Support(roomNumber);
                    roomList.add( room );
                }
            } catch(JSONException j) {
                j.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (categoryData != null) {
            try {
                JSONObject jsonobject = new JSONObject(categoryData);
                Iterator Categ = jsonobject.keys();
                while(Categ.hasNext()){
                    String current = Categ.next().toString();
                    Support currentCategory = new Support(current);
                    categoryList.add( currentCategory );
                    Log.e("FAFA", current);

                    List<Support> currentList = new ArrayList<Support>();
                    JSONArray currentArray = jsonobject.getJSONArray(current);
                    Log.e(Integer.toString(currentArray.length()),currentArray.toString());
                    for (int i = 0; i < currentArray.length(); i++) {
                        Support curSubCateg = new Support(currentArray.getString(i));
                        Log.e("FAFF",currentArray.getString(i));
                        currentList.add( curSubCateg );
                    }

                    subCategoriesMap.put(currentCategory, currentList);
                }

            } catch(JSONException j) {
                j.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        Log.e("FA","Fasd");

        SupportData Data = new SupportData(roomList, categoryList, subCategoriesMap);
        return Data;
    }
}

