package org.hackillinois.android.profile;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.hackillinois.android.models.Skill;
import org.hackillinois.android.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Will Hennessy
 */
public class SkillsDataLoader extends AsyncTaskLoader<ArrayList<Skill>> {

    private URL url;
    private Context mContext;

    public SkillsDataLoader(Context context, URL url) {
        super(context);
        mContext = context;
        this.url = url;
    }

    /**
     */
    @Override
    public ArrayList<Skill> loadInBackground() {
        String data = null; // this will hold the JSON in a string representation
        // try loading the URL
        try{
            HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
            data = httpUtils.loadData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null){
            ArrayList<Skill> skills = new ArrayList<Skill>();
            try {
                final JSONArray jsonArray = new JSONArray(data);

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject skill = jsonArray.getJSONObject(i);
                    skills.add(new Skill(skill));
                }

                return skills;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
