package org.hackillinois.android.profile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Person> {

    private static final String TAG = "ProfileFragment";
    private SkillsAdapter mSkillsAdapter;

    public static ProfileFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                Log.i(TAG, "in broadcast receiver");
                getLoaderManager().initLoader(0, null, ProfileFragment.this).forceLoad();
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        }
    };


    /** launch the DialogFragment to edit skills list **/
    private void launchEditSkillsFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        SkillsDialogFragment fragment = new SkillsDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Hackillinois_Launcher);
//        fragment.show(fragmentTransaction, "skills");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null, false);
        TextView mNameTextView = (TextView) v.findViewById(R.id.name_profile);
        Activity activity = getActivity();
        Utils.setInsets(activity, v);
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_login));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        ListView skillsList = (ListView) v.findViewById(R.id.profile_skills_list);
        mSkillsAdapter = new SkillsAdapter(activity);
        skillsList.setAdapter(mSkillsAdapter);

        //launchEditSkillsFragment();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(Utils.ARG_SECTION_NUMBER));

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0,null,this).forceLoad();
    }

    @Override
    public Loader<Person> onCreateLoader(int id, Bundle args) {
        return new ProfileDataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person person) {
        if (person != null) {
            if (person.getSkills().isEmpty()) {
                launchEditSkillsFragment();
            }
            else {
                List<String> skills = person.getSkills();

                ArrayList<List<String>> lists = new ArrayList<List<String>>();

                int i = 0;
                for(String skill : skills) {
                    if(i % 4 == 0)
                        lists.add( new ArrayList<String>() );
                    lists.get(i/4).add(skill);
                    i++;
                }

                for( ; i % 4 != 0; i++) { // fill in the rest of the 4-tuple with empty strings
                    lists.get(i / 4).add("");
                }

                mSkillsAdapter.clear();
                for(List<String> list : lists)
                    mSkillsAdapter.add(list);
                mSkillsAdapter.notifyDataSetChanged();

            }
            Log.e("profilefragment", person.getName());
        }
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {

    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }
}
