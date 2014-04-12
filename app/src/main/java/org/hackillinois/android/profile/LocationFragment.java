package org.hackillinois.android.profile;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Location;
import org.hackillinois.android.utils.HttpUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends DialogFragment implements
        LoaderManager.LoaderCallbacks<List<Location>>,
        AdapterView.OnItemClickListener {

    private boolean loaded = false;

    private List<Location> locations = new ArrayList<Location>();

    private ListView mListView;
    private ListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, locations);
    }


    public void onStart(){
        super.onStart();
          // This means this is the profile tab so we have to load the data
        if(!loaded) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locationfragment_list, container, false);
        getDialog().setTitle("Please Select a Location");
        assert view != null;
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public Loader<List<Location>> onCreateLoader(int i, Bundle bundle) {
        try {
            return new LocationDataLoader(getActivity());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onLoadFinished(Loader<List<Location>> loader, List<Location> location) {

        if (location != null) {
            loaded = true;
            for(Location l : location){
                locations.add(l);
            }
            mAdapter = new ArrayAdapter<Location>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, locations);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Location>> loader) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedLocation = locations.get(position).toString();
        String body = "\"" + selectedLocation + "\"";
        ProfileFragment profileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag("profileFrag");

        PostTask postTask = new PostTask(getActivity(), "homebase", body);
        postTask.execute();
        profileFragment.setLocation(selectedLocation);
        dismiss();
    }

    public class PostTask extends AsyncTask<String, Integer, Boolean> {

        private Context mContext;
        private String body;
        private String key;

        public PostTask(Context context, String key, String body) {
            mContext = context;
            this.body = body;
            this.key = key;
        }

        @Override
        protected Boolean doInBackground(String... s) {
            try {
                HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);
                return httpUtils.postPersonData(key, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}


