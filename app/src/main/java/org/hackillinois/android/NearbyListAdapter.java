package org.hackillinois.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;

import java.util.List;

public class NearbyListAdapter extends ArrayAdapter<Person> {

    private LayoutInflater mLayoutInflater;

    public NearbyListAdapter(Context context) {
        super(context, R.layout.profile_list_item, R.id.profile_list_item_name);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.profile_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.profile_list_item_image);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.profile_list_item_name);
            viewHolder.companyTextView = (TextView) convertView.findViewById(R.id.profile_list_item_company);
            viewHolder.jobTitleTextView = (TextView) convertView.findViewById(R.id.profile_list_item_job_title);
            viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.profile_list_item_job_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Person person = getItem(position);
        if (person != null) {
            viewHolder.nameTextView.setText(person.getName());
            if (person instanceof Staff ) {
                viewHolder.companyTextView.setText(((Staff) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Staff) person).getJobTitle());
            } else if (person instanceof Mentor) {
                viewHolder.companyTextView.setText(((Mentor) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Mentor) person).getJobTitle());
            } else if (person instanceof Hacker) {
                viewHolder.companyTextView.setText(((Hacker) person).getSchool());
                viewHolder.jobTitleTextView.setText(((Hacker) person).getYear());

            }
            viewHolder.locationTextView.setText(person.getHomebase());
        }
        return convertView;
    }


    public void setData(List<Person> data) {
        if (data != null) {
            clear();
            for (Person person : data) {
                add(person);
            }
        }
    }

    static class ViewHolder {
        public ImageView profileImageView;
        public TextView nameTextView;
        public TextView companyTextView;
        public TextView jobTitleTextView;
        public TextView locationTextView;
    }
}
