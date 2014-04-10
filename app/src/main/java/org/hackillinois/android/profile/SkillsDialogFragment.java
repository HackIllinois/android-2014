package org.hackillinois.android.profile;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Skill;
import org.hackillinois.android.models.people.Person;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SkillsDialogFragment extends DialogFragment {

    private SkillsListAdapter mSkillsListAdapter;

    public static SkillsDialogFragment newInstance(Person person) {
        SkillsDialogFragment fragment = new SkillsDialogFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_skills, container, false);
        mSkillsListAdapter = new SkillsListAdapter(getActivity());

        mSkillsListAdapter.add(new Skill("Java", null, null));
        mSkillsListAdapter.add(new Skill("Python", null, null));
        mSkillsListAdapter.add(new Skill("Android", null, null));

        ListView skillsList = (ListView) rootView.findViewById(R.id.pick_skills_list);
        skillsList.setAdapter(mSkillsListAdapter);
        return rootView;
    }


}