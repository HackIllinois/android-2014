package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.hackillinois.android.R;

import java.util.List;

/**
 * Created by Jonathan on 4/9/14.
 */
public class SkillsAdapter extends ArrayAdapter<List<String>> {

    private LayoutInflater mLayoutInflater;

    private class ViewHolder {
        TextView textView0;
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    public SkillsAdapter(Activity activity) {
        super(activity, R.layout.skills_list_item, R.id.skill_1);
        mLayoutInflater = activity.getLayoutInflater();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = mLayoutInflater.inflate(R.layout.skills_list_item, parent, false);

        TextView textView0 = (TextView) rowView.findViewById(R.id.skill_0);
        TextView textView1 = (TextView) rowView.findViewById(R.id.skill_1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.skill_2);
        TextView textView3 = (TextView) rowView.findViewById(R.id.skill_3);

        List<String> skills = getItem(position);

        /** Set the text for all four skills in this row.
         *  If the skill is an empty string, make the blue box invisible */
        textView0.setText(skills.get(0));
        if(skills.get(1).isEmpty())
            textView1.setVisibility(View.INVISIBLE);
        else
            textView1.setText(skills.get(1));

        if(skills.get(2).isEmpty())
            textView2.setVisibility(View.INVISIBLE);
        else
            textView2.setText(skills.get(2));

        if(skills.get(3).isEmpty())
            textView3.setVisibility(View.INVISIBLE);
        else
            textView3.setText(skills.get(3));

        return rowView;
    }
}