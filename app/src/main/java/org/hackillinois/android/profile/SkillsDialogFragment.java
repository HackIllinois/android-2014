package org.hackillinois.android.profile;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Person;

/**
 * @author  Will Hennessy
 *         SkillsDialogFragment -- edit the user profile skills list
 */

public class SkillsDialogFragment extends DialogFragment {


    public static SkillsDialogFragment newInstance(Person person) {
        SkillsDialogFragment fragment = new SkillsDialogFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_skills, container, false);
        return rootView;
    }


}