package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
    private ArrayList<Skill> allSkills;                 // a read-only reference to all the skills
    private ArrayList<Skill> currentFilteredSkills;     // an edited list of skills to display currently

    /** Constructor **/
    public SkillsListAdapter(Activity activity, ArrayList<Skill> allSkills) {
        super(activity, R.layout.pick_skills_list_item, R.id.pick_skills_list_item_name, allSkills);
        mLayoutInflater = activity.getLayoutInflater();
        this.currentFilteredSkills = allSkills;  // currentFilteredSkills is bound to the list view
        this.allSkills = new ArrayList<Skill>();         // allSkills is a separate copy of the list. It does not affect listview content
        for(Skill skill : allSkills)
            allSkills.add(skill);
    }


    public void setData(ArrayList<Skill> allSkills) {
        //addAll(allSkills);
        this.allSkills = allSkills;
        this.currentFilteredSkills.clear();
        for(Skill skill : allSkills)
            this.currentFilteredSkills.add(skill);
        notifyDataSetChanged();
    }

    /** Function that allows the parent fragment to notify this list adapter when
     *  an item in the list is clicked.  Flip it's selected state.
     * @param position
     */
    public void notifyListItemClick(int position) {
        Skill selected = this.allSkills.get(position);
        selected.setSelected( !selected.isSelected() );
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.pick_skills_list_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.pick_skills_list_item_name);
            holder.image = (ImageView) rowView.findViewById(R.id.pick_skills_list_item_image);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Skill skill = currentFilteredSkills.get(position);

        holder.name.setText( skill.getName() );
        if(skill.isSelected())
            holder.image.setVisibility(View.VISIBLE);
        else
            holder.image.setVisibility(View.INVISIBLE);

        return rowView;
    }


    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public TextView name;
        public ImageView image;
    }



    /** Skills list search filtering **/
    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraintParam) {

                FilterResults results = new FilterResults();
                ArrayList<Skill> filteredSkills = new ArrayList<Skill>();

                // perform your search here using the searchConstraint String.
                String constraint = constraintParam.toString().toLowerCase();

                for (Skill skill : allSkills) {
                    if (skill.isMatch(constraint)) filteredSkills.add(skill);
                }

                results.count = filteredSkills.size();
                results.values = filteredSkills;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.values != null) {
                    currentFilteredSkills.clear();
                    currentFilteredSkills.addAll((ArrayList<Skill>) results.values);
                    notifyDataSetChanged();
                }
            }

        };
    }

}
