package org.hackillinois.android.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
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

import org.hackillinois.android.LoadingInterface;
import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.RoundedTransformation;
import org.hackillinois.android.models.Skill;
import org.hackillinois.android.models.Status;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;
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
    private TextView mTextLocation;
    private TextView mInitials;
    private Picasso mPicasso;
    private LoadingInterface mLoadingInterface;

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
            ArrayList<Skill> skills = (ArrayList<Skill>) intent.getSerializableExtra("skills");

            ArrayList<List<String>> lists = new ArrayList<List<String>>();
            List<String> updatedSkills = new ArrayList<String>();
            int i = 0;
            for (Skill skill : skills) {
                updatedSkills.add(skill.getName());
                if (i % 4 == 0)
                    lists.add(new ArrayList<String>());
                lists.get(i / 4).add(skill.getName());
                i++;
            }

            mPerson.setSkills(updatedSkills);

            for (; i % 4 != 0; i++) { // fill in the rest of the 4-tuple with empty strings
                lists.get(i / 4).add("");
            }

            mSkillsAdapter.clear();
            for (List<String> list : lists)
                mSkillsAdapter.add(list);
            mSkillsAdapter.notifyDataSetChanged();
        }
    };

    /**
     * Launch the DialogFragment to edit skills list. Give it the Person object *
     */
    private void launchEditSkillsFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment skillsFragment = SkillsDialogFragment.newInstance(mPerson);
        skillsFragment.show(fragmentManager, SKILLS_FRAG);
    }

    private void updateStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.update_status_title));
        final CharSequence[] staffStatuses = new CharSequence[]{"Hacking", "Available", "Taking A Break", "Pooping"};
        final CharSequence[] statuses = new CharSequence[]{"Hacking", "Available", "Taking A Break", "Do not distrub"};

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPerson instanceof Staff) {
                    updateStatus(staffStatuses[which].toString());
                } else {
                    updateStatus(statuses[which].toString());
                }
            }
        };
        if (mPerson instanceof Staff) {
            builder.setItems(staffStatuses, clickListener).create().show();
        } else {
            builder.setItems(statuses, clickListener).create().show();
        }
    }


    private void updateStatus(String status) {
        DateTime date = new DateTime();
        if (mPerson == null) {
            return;
        }
        String statusArray = mPerson.getStatusArray();
        Log.e("status array", statusArray.substring(1));
        String body = "[{\"status\": \"" + status + "\", \"date\": " + date.getMillis() / 1000 + "}";
        if (statusArray.length() <= 2)
            body = body + "]";
        else
            body = body + ", " + statusArray.substring(1);
        PostTask postTask = new PostTask(getActivity(), "status", body);
        postTask.execute();
    }

    public void setLocation(String location) {
        mTextLocation.setText(location);
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
        mInitials = (TextView) v.findViewById(R.id.profile_other_initials);
        mPicasso = Picasso.with(getActivity());

        TextView textSkills = (TextView) v.findViewById(R.id.text_skills_header);
        ImageView statusPlusImage = (ImageView) v.findViewById(R.id.profile_status_plus);
        ImageView skillsPlusImage = (ImageView) v.findViewById(R.id.profile_skills_plus);
        ListView skillsList = (ListView) v.findViewById(R.id.profile_skills_list);
        ListView statusList = (ListView) v.findViewById(R.id.status_list);

        Utils.setInsets(getActivity(), v);
        IntentFilter intentFilter = new IntentFilter("get_skills");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        mSkillsAdapter = new SkillsAdapter(getActivity());
        mStatusAdapter = new StatusListAdapter(getActivity());
        skillsList.setAdapter(mSkillsAdapter);
        statusList.setAdapter(mStatusAdapter);

        Object object = getArguments().getSerializable("person");
        if (object != null) {
            mPerson = (Person) object;
            setFields(mPerson);
            List<String> skills = mPerson.getSkills();

            ArrayList<List<String>> lists = new ArrayList<List<String>>();

            int i = 0;
            for (String skill : skills) {
                if (i % 4 == 0)
                    lists.add(new ArrayList<String>());
                lists.get(i / 4).add(skill);
                i++;
            }

            for (; i % 4 != 0; i++) { // fill in the rest of the 4-tuple with empty strings
                lists.get(i / 4).add("");
            }

            List<Status> status_list = mPerson.getStatuses();
            mStatusAdapter.clear();
            for (Status stat : status_list) {
                mStatusAdapter.add(stat);
            }
            mStatusAdapter.notifyDataSetChanged();

            mSkillsAdapter.clear();
            for (List<String> list : lists)
                mSkillsAdapter.add(list);
            mSkillsAdapter.notifyDataSetChanged();

            statusPlusImage.setVisibility(View.GONE);
            skillsPlusImage.setVisibility(View.GONE);

        } else {
            mLoadingInterface.onLoadStart();
            getLoaderManager().initLoader(0, null, this).forceLoad();

            mTextLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchSetLocation();
                }
            });
            textSkills.setOnClickListener(new View.OnClickListener() {
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

            statusPlusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatusDialog();
                }
            });

            skillsPlusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchEditSkillsFragment();
                }
            });
            statusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    updateStatusDialog();
                }
            });
        }
        return v;
    }

    private void launchSetLocation() {
        new LocationFragment().show(getFragmentManager(), "unused");
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onSectionAttached(args.getInt(Utils.ARG_SECTION_NUMBER));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLoadingInterface != null) {
            mLoadingInterface.onLoadEnd();
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

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mLoadingInterface = (LoadingInterface) activity;
        } catch(ClassCastException e){
            throw new ClassCastException("Activity must implement LoadingInterface.");
        }
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person person) {

        if (person != null) {
            List<String> skills = person.getSkills();

            ArrayList<List<String>> lists = new ArrayList<List<String>>();

            int i = 0;
            for (String skill : skills) {
                if (i % 4 == 0)
                    lists.add(new ArrayList<String>());
                lists.get(i / 4).add(skill);
                i++;
            }

            for (; i % 4 != 0; i++) { // fill in the rest of the 4-tuple with empty strings
                lists.get(i / 4).add("");
            }

            List<Status> status_list = person.getStatuses();
            mStatusAdapter.clear();
            for (Status stat : status_list) {
                mStatusAdapter.add(stat);
            }
            mStatusAdapter.notifyDataSetChanged();

            mSkillsAdapter.clear();
            for (List<String> list : lists)
                mSkillsAdapter.add(list);
            mSkillsAdapter.notifyDataSetChanged();


            setFields(person);
            mPerson = person;
            if (person.getStatuses().isEmpty())
                updateStatus("Hacking");

        }
        mLoadingInterface.onLoadEnd();
    }

    private void setFields(Person person) {
        mNameTextView.setText(person.getName());

        Resources res = getResources();
        RoundedTransformation mBlueTransformation = new RoundedTransformation(100, 5,
                res.getColor(R.color.hackillinois_blue_trans), 0);
        if (!person.getFbID().isEmpty())
            mPicasso.load(person.getImageURL()).resize(200, 200).centerCrop()
                    .transform(mBlueTransformation).into(mImageView);
        else {
            mImageView.setVisibility(View.INVISIBLE);
            mInitials.setVisibility(View.VISIBLE);
            String parseThisShit = person.getName();
            int space = parseThisShit.indexOf(" ");
            String firstName = parseThisShit.substring(0, 1);
            String lastName = parseThisShit.substring(space + 1, space + 2);
            String initials = firstName.toUpperCase() + lastName.toUpperCase();
            mInitials.setText(initials);
        }

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

    public class PostTask extends AsyncTask<String, Void, Boolean> {

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

        @Override
        protected void onPostExecute(Boolean thing) {
            super.onPostExecute(thing);
            getLoaderManager().initLoader(0, null, ProfileFragment.this).forceLoad();
        }
    }

    public void setInterface(LoadingInterface loadingInterface) {
        mLoadingInterface = loadingInterface;
    }
}
