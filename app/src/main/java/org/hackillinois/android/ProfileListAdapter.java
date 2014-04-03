package org.hackillinois.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Stephen on 3/31/14.
 */
public class ProfileListAdapter extends ArrayAdapter<PersonItem>
{
    private ArrayList<PersonItem> peopleList;
    private LayoutInflater mLayoutInflater;

    public ProfileListAdapter(Activity activity, ArrayList<PersonItem> peopleList)
    {
        super(activity, 0, peopleList);
        this.peopleList = peopleList;
        this.mLayoutInflater = activity.getLayoutInflater();
    }

    static class ViewHolder
    {
        public ImageView profile_image;
        public TextView name;
        public TextView company;
        public TextView job_title;
        public TextView location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;
        ViewHolder holder;

        /* Try to reuse a row view that is out of sight */
        if(rowView == null)
        {
            rowView = mLayoutInflater.inflate(R.layout.profile_list_item, null);

            holder = new ViewHolder();
            holder.profile_image = (ImageView) rowView.findViewById(R.id.profile_list_item_image);
            holder.name = (TextView) rowView.findViewById(R.id.profile_list_item_name);
            holder.company = (TextView) rowView.findViewById(R.id.profile_list_item_company);
            holder.job_title = (TextView) rowView.findViewById(R.id.profile_list_item_job_title);
            holder.location = (TextView) rowView.findViewById(R.id.profile_list_item_job_title);
            rowView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) rowView.getTag();
        }

        PersonItem personItem = peopleList.get(position);

        holder.name.setText(personItem.getName());
        holder.company.setText(personItem.getCompany());
        holder.job_title.setText(personItem.getJobTitle());
        holder.location.setText(personItem.getLocation());

        Picasso.with(getContext())
                .load(personItem.getPictureURL())
                .into(holder.profile_image);

        return rowView;
    }

}
