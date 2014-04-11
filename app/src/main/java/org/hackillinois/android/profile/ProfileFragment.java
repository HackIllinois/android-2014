package org.hackillinois.android.profile;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.Status;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.HttpUtils;
import org.hackillinois.android.utils.Utils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Person> {

    private static final String SKILLS_FRAG = "EDIT_SKILLS";
    private static final String TAG = "ProfileFragment";

    private Person mPerson;
    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mTextSchool;
    private TextView mTextSkills;
    private TextView mTextLocation;
    private Picasso mPicasso;

    private SkillsAdapter mSkillsAdapter;
    private StatusListAdapter mStatusAdapter;

    public static ProfileFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        args.putInt(Utils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newViewInstance(Person person) {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        args.putSerializable("person", person);
        fragment.setArguments(args);
        return fragment;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLoaderManager() != null) {
                getLoaderManager().initLoader(0, null, ProfileFragment.this).forceLoad();
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        }
    };

    /** Launch the DialogFragment to edit skills list. Give it the Person object **/
    private void launchEditSkillsFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment skillsFragment = SkillsDialogFragment.newInstance(null);
        skillsFragment.show(fragmentManager, SKILLS_FRAG);

//        Fragment profileFragment = fragmentManager.findFragmentByTag(SKILLS_FRAG);
//        if (profileFragment == null) {
//            profileFragment = new SkillsDialogFragment();
//        }
//        fragmentManager.beginTransaction().replace(R.id.container, profileFragment, SKILLS_FRAG).addToBackStack(null)
//                .commit();
//        fragmentManager.executePendingTransactions();
    }

    private void updateStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Hacker Status");
        builder.setItems(new CharSequence[]
                        {"Hacking", "Available", "Taking A Break", "Do Not Disturb"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                updateStatus("Hacking");
                                break;
                            case 1:
                                updateStatus("Available");
                                break;
                            case 2:
                                updateStatus("Taking A Break");
                                break;
                            case 3:
                                updateStatus("Do Not Disturb");
                                break;
                        }
                    }
                }
        );
        builder.create().show();
    }

    private void updateStatus(String status){
        DateTime date = new DateTime();
        String statusArray = mPerson.getStatusArray().toString();
        Log.e("status array", statusArray.substring(1));
        String body = "[{\"status\": \"" + status + "\", \"date\": " + date.getMillis()/1000 + "}";
        if(statusArray.length() <= 2)
            body = body + "]";
        else
            body = body + ", " + statusArray.substring(1);
        PostTask postTask = new PostTask(getActivity(), "status", mPerson.getType(), body);
        postTask.execute();
    }

    public void setLocation() {
        getLoaderManager().initLoader(0,null,this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null, false);
        assert v != null;
        mImageView = (ImageView) v.findViewById(R.id.profile_image);
        mNameTextView = (TextView) v.findViewById(R.id.name_profile);
        mTextSchool = (TextView) v.findViewById(R.id.school_profile);
        mTextLocation = (TextView) v.findViewById(R.id.location_profile);
        mTextSkills = (TextView) v.findViewById(R.id.text_skills_header);
        mPicasso = Picasso.with(getActivity());

        ListView skillsList = (ListView) v.findViewById(R.id.profile_skills_list);
        ListView statusList = (ListView) v.findViewById(R.id.status_list);


        Object object = getArguments().getSerializable("person");
        if (object != null) {
            mPerson = (Person) object;
            setFields(mPerson);
        }

        Utils.setInsets(getActivity(), v);
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast_login));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        mSkillsAdapter = new SkillsAdapter(getActivity());
        skillsList.setAdapter(mSkillsAdapter);
        mStatusAdapter = new StatusListAdapter(getActivity());
        statusList.setAdapter(mStatusAdapter);


        mTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSetLocation();
            }
        });

        mTextSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditSkillsFragment();
            }
        });
        skillsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditSkillsFragment();
            }
        });
        statusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateStatusDialog();
            }
        });
        return v;
    }

    private void launchSetLocation() {
        getFragmentManager().beginTransaction().replace(R.id.container, new LocationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            if (args.getInt(Utils.ARG_SECTION_NUMBER) != 0) {
                ((MainActivity) getActivity()).onSectionAttached(args.getInt(Utils.ARG_SECTION_NUMBER));
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPerson == null) {
            // This means this is the profile tab so we have to load the data
            getLoaderManager().initLoader(0,null,this).forceLoad();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public Loader<Person> onCreateLoader(int id, Bundle args) {
        return new ProfileDataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person person) {
        if (person != null) {
            if (person.getSkills().isEmpty()) {
                // maybe launch skills fragment
                // launchEditSkillsFragment();
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

                List<Status> status_list = person.getStatuses();
                mStatusAdapter.clear();
                for(Status stat : status_list) {
                    mStatusAdapter.add(stat);
                }
                mStatusAdapter.notifyDataSetChanged();

                mSkillsAdapter.clear();
                for(List<String> list : lists)
                    mSkillsAdapter.add(list);
                mSkillsAdapter.notifyDataSetChanged();


            }
            setFields(person);
            mPerson = person;
            if(person.getStatuses().isEmpty())
                updateStatus("Hacking");
        }
    }

    private void setFields(Person person) {
        mNameTextView.setText(person.getName());
        mPicasso.load(person.getImageURL()).resize(200, 200).centerCrop().into(mImageView);
        if (person instanceof Hacker)
            mTextSchool.setText(((Hacker) person).getSchool());
        else if (person instanceof Mentor)
            mTextSchool.setText(((Mentor) person).getCompany());
        if (person.getHomebase() == null || person.getHomebase().isEmpty())
            mTextLocation.setText(R.string.set_location);
        else
            mTextLocation.setText(person.getHomebase());
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
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

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            getLoaderManager().initLoader(0,null,ProfileFragment.this).forceLoad();
        }
    }

    public Person getmPerson() {
        return mPerson;
    }
}
