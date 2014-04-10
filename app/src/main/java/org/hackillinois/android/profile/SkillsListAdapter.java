package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Skill;

import java.util.ArrayList;

/**
 * @author Will Hennessy
 *
 *  ArrayAdapter class to back the Newsfeed list view.
 *  Uses lazy loading ViewHolders to optimize scrolling efficiency.
 */
public class SkillsListAdapter extends ArrayAdapter<Skill> implements Filterable {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Skill> allSkills;                 // a read-only copy of all the skills
    private ArrayList<Skill> currentFilteredSkills;     // an edited list of skills to display currently

    /** Constructor **/
    public SkillsListAdapter(Activity activity, ArrayList<Skill> allSkills) {
        super(activity, R.layout.pick_skills_list_item, R.id.pick_skills_list_item_name, allSkills);
        mLayoutInflater = activity.getLayoutInflater();
        this.allSkills = (ArrayList<Skill>) allSkills.clone();
        this.currentFilteredSkills = allSkills;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.pick_skills_list_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.pick_skills_list_item_name);
            holder.checkbox = (CheckBox) rowView.findViewById(R.id.pick_skills_list_item_checkbox);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Skill skill = currentFilteredSkills.get(position);

        holder.name.setText( skill.getName() );
        holder.checkbox.setChecked(false);

        return rowView;
    }


    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public TextView name;
        public CheckBox checkbox;
    }



    /** Skills list search filtering **/
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraintParam) {

                FilterResults results = new FilterResults();
                ArrayList<Skill> filteredSkills = new ArrayList<Skill>();

                // perform your search here using the searchConstraint String.
                String constraint = constraintParam.toString().toLowerCase();

                for (int i = 0; i < allSkills.size(); i++) {
                    Skill skill = allSkills.get(i);
                    if ( skill.isMatch(constraint) )  {
                        filteredSkills.add(skill);
                    }
                }

                results.count = filteredSkills.size();
                results.values = filteredSkills;

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                currentFilteredSkills.clear();
                currentFilteredSkills.addAll((ArrayList<Skill>) results.values);
                notifyDataSetChanged();
            }

        };

        return filter;
    }

}
