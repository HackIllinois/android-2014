package org.hackillinois.android.profile;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Location;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.HttpUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Location>>,
        AdapterView.OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private boolean loaded = false;

    private List<Location> locations = new ArrayList<Location>();


    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Change Adapter to display your content
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


    public void onResume(){
        super.onResume();
        if(!loaded){
                // This means this is the profile tab so we have to load the data
                getLoaderManager().initLoader(0,null,this).forceLoad();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locationfragment, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
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
                Log.i("DataFromLoader",l.toString());
            }
            mAdapter = new ArrayAdapter<Location>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, locations);
            mListView.setAdapter(mAdapter);
        } else{
            Log.e("LocationLoader", "No list of locations");
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
        Person mPerson = profileFragment.getmPerson();

        PostTask postTask = new PostTask(getActivity(), "homebase", mPerson.getType(), body);
        postTask.execute();

        profileFragment.setLocation();
        getFragmentManager().popBackStack();
    }

    public class PostTask extends AsyncTask<String, Integer, Integer> {

        private Context mContext;
        private String body;
        private String key;
        private String type;

        private final Integer POST_SUCCESS = 0x1;
        private final Integer POST_FAIL = 0x0;

        public PostTask(Context context, String key, String type, String body) {
            mContext = context;
            this.body = body;
            this.key = key;
            this.type = type;
        }


        @Override
        protected Integer doInBackground(String... s) {

            try {
                HttpUtils httpUtils = HttpUtils.getHttpUtils(mContext);

                httpUtils.postPersonData(key, body, type);

                return POST_SUCCESS;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return POST_FAIL;
        }

//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//            getLoaderManager().initLoader(0,null,ProfileFragment.this).forceLoad();
//        }
    }





}


