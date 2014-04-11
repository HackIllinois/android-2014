package org.hackillinois.android.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
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
    private SupportData MainData;

    public static SupportFragment newInstance(int sectionNumber) {
            Bundle args = new Bundle();
            SupportFragment fragment = new SupportFragment();
            args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        mSupportListAdapter = new SupportListAdapter(getActivity());
        final ListView supportList = (ListView) rootView.findViewById(R.id.support_list);
        supportList.setAdapter(mSupportListAdapter);

        getLoaderManager().initLoader(0, null, this).forceLoad();


        //Initially set up ListView
        List<Support> modCategories = new ArrayList<Support>();
        for (Support s : categories) {
            modCategories.add(s);
        }
        Support Call = new Support("Call for URGENT help only.");
        modCategories.add(Call);

        mSupportListAdapter.setData(modCategories);

    supportList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                boolean selectionFound = false;

                String selected = mSupportListAdapter.getItem(position).getTitle();

                if (selected.equalsIgnoreCase("Call for URGENT help only.")) {

                    selectionFound = true;

                    List<Support> modCategories = new ArrayList<Support>();
                    for (Support s : categories) {
                        modCategories.add(s);
                    }
                    Support Call = new Support("2104217555 for URGENT need only");
                    modCategories.add(Call);

                    mSupportListAdapter.setData(modCategories);
                    /***
                     * JUST GIVES NUMBER FOR NOW.
                     * IF IT IS CHANGED DON'T FROGET TO UPDATE MANIFEST <uses-permission android:name="android.permission.CALL_PHONE" />
                     * CHECK IF DEVICE CAN MAKE CALLS
                     Intent callIntent = new Intent(Intent.ACTION_CALL);
                     callIntent.setData(Uri.parse("tel:2014217555"));
                     startActivity(callIntent);
                     **/
                }
                if (!selectionFound) {
                    if (selected.equalsIgnoreCase("Main")) {

                        selectionFound = true;

                        List<Support> modCategories = new ArrayList<Support>();
                        for (Support s : categories) {
                            modCategories.add(s);
                        }
                        Support Call = new Support("Call for URGENT help only.");
                        modCategories.add(Call);

                        mSupportListAdapter.setData(modCategories);
                    }
                }
                if (!selectionFound) {
                    for (Support item : categories) {
                        if (selected.equalsIgnoreCase(item.getTitle()) || selected.substring(1).equalsIgnoreCase(item.getTitle())) {
                            selectionFound = true;
                            List<Support> modSubCategories = new ArrayList<Support>();

                            //Path
                            Support main = new Support("Main");
                            modSubCategories.add(main);
                            if (selected.equalsIgnoreCase(item.getTitle())) {
                                Support OptionChosen = new Support("-" + selected);
                                modSubCategories.add(OptionChosen);
                            } else {
                                Support OptionChosen = new Support(selected);
                                modSubCategories.add(OptionChosen);
                            }

                            int tempint = -1;
                            for (int i = 0; i < categories.size(); i++) {
                                if(selected.equalsIgnoreCase(categories.get(i).getTitle()) || selected.substring(1).equalsIgnoreCase(categories.get(i).getTitle()))
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
                    }
                }
                if (!selectionFound) {
                        int tempint = -1;
                        for (int i = 0; i < categories.size(); i++) {
                            if(mSupportListAdapter.getItem(1).getTitle().substring(1).equalsIgnoreCase(categories.get(i).getTitle()))
                            {
                                tempint = i;
                            }
                        }
                        if(tempint != -1) {
                            for (Support item : subCategories.get(categories.get(tempint))) {
                                if (selected.equalsIgnoreCase(item.getTitle()) || selected.substring(2).equalsIgnoreCase(item.getTitle())) {
                                    selectionFound = true;
                                    List<Support> modRoomData = new ArrayList<Support>();

                                    Support main = new Support("Main");
                                    modRoomData.add(main);
                                    Support OptionChosen = new Support(mSupportListAdapter.getItem(1).getTitle());
                                    modRoomData.add(OptionChosen);
                                    if (selected.equalsIgnoreCase(item.getTitle())) {
                                        Support SubOptionChosen = new Support("--" + selected);
                                        modRoomData.add(SubOptionChosen);
                                    } else {
                                        Support SubOptionChosen = new Support(selected);
                                        modRoomData.add(SubOptionChosen);
                                    }

                                    for (Support s : rooms) {
                                        modRoomData.add(s);
                                    }

                                    mSupportListAdapter.setData(modRoomData);
                                    break;
                                }
                            }
                        }
                }
                if (!selectionFound) {
                    for (Support item : rooms) {
                        if (selected.equalsIgnoreCase(item.getTitle())) {
                            selectionFound = true;
                            Intent testIntent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:?subject=" + mSupportListAdapter.getItem(1).getTitle() + " - " + mSupportListAdapter.getItem(2).getTitle() + " - " + mSupportListAdapter.getItem(position).getTitle() + "&body=" + "" + "&to=" + "sendme@me.com");
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
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));
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
            Support Call = new Support("Call for URGENT help only.");
            modCategories.add(Call);

            mSupportListAdapter.setData(modCategories);
        }

        @Override
        public void onLoaderReset(Loader<SupportData> loader) {
        }

}