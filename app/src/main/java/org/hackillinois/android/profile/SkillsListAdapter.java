package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Skill;

import java.util.List;

/**
 * @author Will Hennessy
 *
 *  ArrayAdapter class to back the Newsfeed list view.
 *  Uses lazy loading ViewHolders to optimize scrolling efficiency.
 */
public class SkillsListAdapter extends ArrayAdapter<Skill> {

    private LayoutInflater mLayoutInflater;

    /** Constructor **/
    public SkillsListAdapter(Activity activity) {
        super(activity, R.layout.pick_skills_list_item, R.id.pick_skills_list_item_name);
        mLayoutInflater = activity.getLayoutInflater();
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

        Skill skill = getItem(position);

        holder.name.setText( skill.getName() );
        holder.checkbox.setChecked(false);

        return rowView;
    }


    /** Set the data for the list adapter **/
    public void setData(List<Skill> data) {
        if (data != null) {
            clear();
            for (Skill skill : data) {
                add(skill);
            }
        }
    }


    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public TextView name;
        public CheckBox checkbox;
    }

}
