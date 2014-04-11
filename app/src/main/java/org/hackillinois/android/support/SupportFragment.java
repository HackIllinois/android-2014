package org.hackillinois.android.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Support;
import org.hackillinois.android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SupportFragment extends DialogFragment {

    private SupportListAdapter mSupportListAdapter;

        public static SupportFragment newInstance(int sectionNumber) {
            Bundle args = new Bundle();
            SupportFragment fragment = new SupportFragment();
            args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final String Option[];
        final String SubOption1[];
        final String SubOption2[];
        final String SubOption3[];
        final String SubOption[];
        final String Room[];

        /*********************/
        Option = new String[] {"Option1","Option2","Option3"};
        SubOption1 = new String[] {"SubOption11","SubOption12","SubOption13"};
        SubOption2 = new String[] {"SubOption21","SubOption22","SubOption23"};
        SubOption3 = new String[] {"SubOption31","SubOption32","SubOption33"};
        SubOption = new String[] {"SubOption41","SubOption42","SubOption43"};
        Room = new String[] {"Room1","Room2","Room3"};
        /*********************/

        List<Support> OptionData = new ArrayList<Support>();
        for(int i = 0; i < Option.length; i++){
            Support temp = new Support(Option[i]);
            OptionData.add(temp);
        }
        Support Call = new Support("Call for URGENT help only.");
        OptionData.add(Call);


        View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        mSupportListAdapter = new SupportListAdapter(getActivity());
        final ListView supportList = (ListView) rootView.findViewById(R.id.support_list);
        supportList.setAdapter(mSupportListAdapter);

        mSupportListAdapter.setData(OptionData);

        supportList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                boolean selectionFound = false;

                String selected = mSupportListAdapter.getItem(position).getTitle();

                if (selected.equalsIgnoreCase("Call for URGENT help only.")) {
                    selectionFound = true;
                    List<Support> OptionData = new ArrayList<Support>();
                    for (int i = 0; i < Option.length; i++) {
                        Support temp = new Support(Option[i]);
                        OptionData.add(temp);
                    }

                    Support Call = new Support("2104217555 For URGENT need only");
                    OptionData.add(Call);

                    mSupportListAdapter.setData(OptionData);
                        /***
                         * JUST GIVES NUMBER FOR NOW.
                         * IF IT IS CHANGED DON'T FROGET TO UPDATE MANIFEST <uses-permission android:name="android.permission.CALL_PHONE" />
                         * CHECK IF DEVICE CAN MAKE CALLS
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:2014217555"));
                        startActivity(callIntent);
                         **/
                }
                if(!selectionFound) {
                    if (selected.equalsIgnoreCase("Main")) {
                        selectionFound = true;
                        List<Support> OptionData = new ArrayList<Support>();
                        for (int i = 0; i < Option.length; i++) {
                            Support temp = new Support(Option[i]);
                            OptionData.add(temp);
                        }

                        Support Call = new Support("Call for URGENT help only.");
                        OptionData.add(Call);

                        mSupportListAdapter.setData(OptionData);
                    }
                }
                if(!selectionFound) {
                    for (String item : Option) {
                        if (selected.equalsIgnoreCase(item) || selected.substring(1).equalsIgnoreCase(item)) {
                            selectionFound = true;
                            List<Support> SubOptionData = new ArrayList<Support>();

                            Support main = new Support("Main");
                            SubOptionData.add(main);
                            if (selected.equalsIgnoreCase(item)) {
                                Support OptionChosen = new Support("-" + selected);
                                SubOptionData.add(OptionChosen);
                            }
                            else{
                                Support OptionChosen = new Support(selected);
                                SubOptionData.add(OptionChosen);
                            }

                            for (int i = 0; i < Option.length; i++) {
                                Support temp = new Support(SubOption[i]);
                                SubOptionData.add(temp);
                            }
                            mSupportListAdapter.setData(SubOptionData);
                            break;
                        }
                    }
                }
                if(!selectionFound){
                    for (String item : SubOption) {
                        if (selected.equalsIgnoreCase(item)  || selected.substring(2).equalsIgnoreCase(item)) {
                            selectionFound = true;
                            List<Support> RoomData = new ArrayList<Support>();

                            Support main = new Support("Main");
                            RoomData.add(main);
                            Support OptionChosen = new Support(mSupportListAdapter.getItem(1).getTitle());
                            RoomData.add(OptionChosen);
                            if (selected.equalsIgnoreCase(item)) {
                                Support SubOptionChosen = new Support("--" + selected);
                                RoomData.add(SubOptionChosen);
                            }
                            else{
                                Support SubOptionChosen = new Support(selected);
                                RoomData.add(SubOptionChosen);
                            }
                            for(int i =0; i < SubOption.length; i++){
                                Support temp = new Support(Room[i]);
                                RoomData.add(temp);
                            }

                            mSupportListAdapter.setData(RoomData);
                            break;
                        }
                    }
                }
                if(!selectionFound){
                    for (String item : Room) {
                        if (selected.equalsIgnoreCase(item)) {
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


}