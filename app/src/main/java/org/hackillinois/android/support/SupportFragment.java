package org.hackillinois.android.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import org.hackillinois.android.models.Support.Support;
import org.hackillinois.android.models.Support.SupportData;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SupportFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<SupportData>{

    private static final String SUPPORT_ROOM_JSON_URL = "http://www.hackillinois.org/mobile/map";
    private static final String SUPPORT_CATEGORY_JSON_URL = "http://www.hackillinois.org/mobile/support";
    private SupportListAdapter mSupportListAdapter;
    private List<Support> rooms = new ArrayList<Support>();
    private List<Support> categories = new ArrayList<Support>();
    private Map<Support, List<Support>> subCategories = new HashMap<Support, List<Support>>();

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


        //Initially set up ListView
        List<Support> modCategories = new ArrayList<Support>();
        for (Support s : categories) {
            modCategories.add(s);
        }
        
        mSupportListAdapter.setData(modCategories);

        supportList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                boolean selectionFound = false;

                String selected = mSupportListAdapter.getItem(position).getTitle();
                for (Support item : categories) {
                    if (selected.equalsIgnoreCase(item.getTitle())) {
                        selectionFound = true;
                        List<Support> modSubCategories = new ArrayList<Support>();

                        //Path
                        Support OptionChosen = new Support("-" + selected);
                        modSubCategories.add(OptionChosen);

                        int tempint = -1;
                        for (int i = 0; i < categories.size(); i++) {
                            if(selected.equalsIgnoreCase(categories.get(i).getTitle()))
                            {
                                tempint = i;
                            }
                        }
                        if(tempint != -1) {
                            for (Support s : subCategories.get(categories.get(tempint))) {
                                modSubCategories.add(s);
                            }
                        }
                        mSupportListAdapter.setData(modSubCategories);
                        break;
                    }
                    if (selected.substring(1).equalsIgnoreCase(item.getTitle())) {
                        selectionFound = true;
                        List<Support> modCategories = new ArrayList<Support>();
                        for (Support s : categories) {
                            modCategories.add(s);
                        }

                        mSupportListAdapter.setData(modCategories);
                        break;
                    }
                }
                if (!selectionFound) {
                    int tempint = -1;
                    for (int i = 0; i < categories.size(); i++) {
                        if(mSupportListAdapter.getItem(0).getTitle().substring(1).equalsIgnoreCase(categories.get(i).getTitle()))
                        {
                            tempint = i;
                        }
                    }
                    if(tempint != -1) {
                        for (Support item : subCategories.get(categories.get(tempint))) {
                            if (selected.equalsIgnoreCase(item.getTitle())) {
                                selectionFound = true;
                                List<Support> modRoomData = new ArrayList<Support>();

                                Support OptionChosen = new Support(mSupportListAdapter.getItem(0).getTitle());
                                modRoomData.add(OptionChosen);
                                Support SubOptionChosen = new Support("--" + selected);
                                modRoomData.add(SubOptionChosen);

                                for (Support s : rooms) {
                                    modRoomData.add(s);
                                }

                                mSupportListAdapter.setData(modRoomData);
                                break;
                            }
                            if (selected.substring(2).equalsIgnoreCase(item.getTitle())) {
                                selectionFound = true;


                                List<Support> modSubCategories = new ArrayList<Support>();

                                Support OptionChosen = new Support(mSupportListAdapter.getItem(0).getTitle());
                                modSubCategories.add(OptionChosen);

                                for (Support s : subCategories.get(categories.get(tempint))) {
                                    modSubCategories.add(s);
                                }
                                mSupportListAdapter.setData(modSubCategories);
                                break;
                            }
                        }
                    }
                }
                if (!selectionFound) {
                    for (Support item : rooms) {
                        if (selected.equalsIgnoreCase(item.getTitle())) {
                            Intent testIntent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:?subject=" + mSupportListAdapter.getItem(1).getTitle() + " - " + mSupportListAdapter.getItem(2).getTitle() + " - " + mSupportListAdapter.getItem(position).getTitle() + "&body=" + "" + "&to=" + "support@hackillinois.org");
                            testIntent.setData(data);
                            startActivity(testIntent);
                            break;
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
    public Loader<SupportData> onCreateLoader(int id, Bundle args) {
        try {
            return new SupportDataLoader(getActivity(), new URL(SUPPORT_ROOM_JSON_URL), new URL(SUPPORT_CATEGORY_JSON_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<SupportData> loader, SupportData data) {
        rooms = data.getRooms();
        categories = data.getCategories();
        subCategories = data.getSubCategories();

        //Initially set up ListView
        List<Support> modCategories = new ArrayList<Support>();
        for (Support s : categories) {
            modCategories.add(s);
        }

        mSupportListAdapter.setData(modCategories);
    }

    @Override
    public void onLoaderReset(Loader<SupportData> loader) {
    }

}