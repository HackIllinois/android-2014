package org.hackillinois.android.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.Location;
import org.hackillinois.android.models.Support;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author  Ben Fishbein
 *         SupportFragment -- edit the user profile skills list
 */

public class SupportFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Support>{

    private SupportListAdapter mSupportListAdapter;
    private TreeMap<String, List<String>> categories = new TreeMap<String, List<String>>();
    private ArrayList<Location> rooms = new ArrayList<Location>();

    public static SupportFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        SupportFragment fragment = new SupportFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        mSupportListAdapter = new SupportListAdapter(getActivity());
        assert rootView != null;
        final ListView supportList = (ListView) rootView.findViewById(R.id.support_list);
        Utils.setInsets(getActivity(), supportList);
        supportList.setAdapter(mSupportListAdapter);

        getLoaderManager().initLoader(0, null, this).forceLoad();

        List<String> initialCategories = new ArrayList<String>();

        Set set = categories.entrySet();
        // Get an iterator
        Iterator i = set.iterator();

        //Initially set up ListView
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            initialCategories.add(me.getKey().toString());
        }

        mSupportListAdapter.setData(initialCategories);
        supportList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Set set = categories.entrySet();
                // Get an iterator
                Iterator i = set.iterator();

                String selected = mSupportListAdapter.getItem(position).toString();
                List<String> newCategoryList = new ArrayList<String>();
                int size = mSupportListAdapter.getSize();
                while(i.hasNext()) {

                    Map.Entry me = (Map.Entry) i.next();
                    if(selected.equalsIgnoreCase(me.getKey().toString())){
                        for(int x = 0; x < position; x++){
                            newCategoryList.add(mSupportListAdapter.getItem(x));
                        }
                        newCategoryList.add(mSupportListAdapter.getItem(position)+" ");

                        ArrayList cat = (ArrayList) me.getValue();
                        for(int x = 0; x < cat.size(); x ++){
                            newCategoryList.add("- " + cat.get(x).toString());
                        }
                        for(int x = position+1; x < size ;x++){
                            newCategoryList.add(mSupportListAdapter.getItem(x));
                        }
                        mSupportListAdapter.setData(newCategoryList);
                        break;
                    }
                    if(selected.equalsIgnoreCase(me.getKey().toString()+ " ")){
                        for(int x = 0; x < position; x++){
                            newCategoryList.add(mSupportListAdapter.getItem(x));
                        }
                        //removes the " " flag
                        newCategoryList.add(mSupportListAdapter.getItem(position).substring(0,mSupportListAdapter.getItem(position).length()-1));
                        ArrayList cat = (ArrayList) me.getValue();
                        for(int x = position+1+cat.size(); x < size ;x++){
                            newCategoryList.add(mSupportListAdapter.getItem(x));
                        }
                        mSupportListAdapter.setData(newCategoryList);
                        break;
                    }
                    ArrayList cat = (ArrayList) me.getValue();
                    for(int x = 0; x < cat.size() ;x++){
                        if (selected.toUpperCase().contains(cat.get(x).toString().toUpperCase())) {
                            Bundle bundle=new Bundle();
                            bundle.putString("category", me.getKey().toString());
                            bundle.putString("subCategory", cat.get(x).toString());
                            LocationFragment locFrag = new LocationFragment();
                            locFrag.setArguments(bundle);
                            locFrag.show(getFragmentManager(), "unused");
                        }
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.support, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7077223706"));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Support> onCreateLoader(int id, Bundle args) {
        try {
            return new SupportDataLoader(getActivity());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Support> loader, Support data) {
        categories = data.getCategories();

        List<String> initialCategories = new ArrayList<String>();

        Set set = categories.entrySet();
        Iterator i = set.iterator();

        //Initially set up ListView
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            initialCategories.add(me.getKey().toString());
        }
        mSupportListAdapter.setData(initialCategories);
    }

    @Override
    public void onLoaderReset(Loader<Support> loader) {
    }

}