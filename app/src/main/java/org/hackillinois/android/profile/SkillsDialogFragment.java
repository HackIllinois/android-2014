package org.hackillinois.android.profile;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Skill;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SkillsDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<ArrayList<Skill>> {

    private static final String SKILLS_URL = "http://www.hackillinois.org/mobile/skills";

    private SkillsListAdapter mSkillsListAdapter;
    private Person person;

    public static SkillsDialogFragment newInstance(Person person) {
        SkillsDialogFragment fragment = new SkillsDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Hackillinois_Skills);
        Bundle args = new Bundle();
        args.putSerializable("person", person);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_skills, container, false);
        person = (Person) getArguments().getSerializable("person");

        Utils.setInsetsBottom(getActivity(), rootView);
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ListView skillsList = (ListView) rootView.findViewById(R.id.pick_skills_list);
        mSkillsListAdapter = new SkillsListAdapter(getActivity(), new ArrayList<Skill>());
        skillsList.setAdapter(mSkillsListAdapter);

        // Set the list OnItemClickListener
        skillsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSkillsListAdapter.notifyListItemClick(position);
            }
        });

        // Set the Done TextView onClickListener
        TextView done = (TextView) rootView.findViewById(R.id.pick_skills_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do you need to do something with this?
                ArrayList<Skill> selected = mSkillsListAdapter.getSelectedSkills();
            }
        });


        EditText input = (EditText) rootView.findViewById(R.id.pick_skills_edittext);
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the text
                mSkillsListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        return rootView;
    }




    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, SkillsDialogFragment.this).forceLoad();
    }

    @Override
    public void onDetach() {
        getLoaderManager().destroyLoader(0);
        super.onDetach();
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<Skill>> onCreateLoader(int id, Bundle args) {
        try {
            return new SkillsDataLoader(getActivity(), new URL(SKILLS_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link android.support.v4.app.FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     *
     * @param loader The Loader that has finished.
     * @param allSkills   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Skill>> loader, ArrayList<Skill> allSkills) {
        // load data into the list
        ArrayList<String> userSkills = (ArrayList<String>) person.getSkills();

        // Pre-select all of the skills tha the user has already selected
        if(userSkills.size() > 0) {
            for (Skill skill : allSkills) {
                if (userSkills.contains(skill.getName()))
                    skill.setSelected(true);
            }
        }

        mSkillsListAdapter.setData( allSkills );
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<Skill>> loader) {

    }

}