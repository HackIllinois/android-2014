package org.hackillinois.android.profile;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Skill;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.Utils;

import java.util.ArrayList;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SkillsDialogFragment extends DialogFragment {

    private SkillsListAdapter mSkillsListAdapter;

    public static SkillsDialogFragment newInstance(Person person) {
        SkillsDialogFragment fragment = new SkillsDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Hackillinois_Skills);
        Log.e("HI", "newinstance");
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_skills, container, false);

        Utils.setInsetsBottom(getActivity(), rootView);
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("Java", new ArrayList<String>(), new ArrayList<String>()));      // mock data
        skills.add(new Skill("Python", new ArrayList<String>(), new ArrayList<String>()));
        skills.add(new Skill("Android", new ArrayList<String>(), new ArrayList<String>()));

        mSkillsListAdapter = new SkillsListAdapter(getActivity(), skills);

        ListView skillsList = (ListView) rootView.findViewById(R.id.pick_skills_list);
        skillsList.setAdapter(mSkillsListAdapter);

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

}