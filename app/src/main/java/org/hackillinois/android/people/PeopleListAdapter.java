package org.hackillinois.android.people;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.hackillinois.android.R;
import org.hackillinois.android.RoundedTransformation;
import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.models.people.Staff;

import java.util.List;

public class PeopleListAdapter extends ArrayAdapter<Person> {

    private LayoutInflater mLayoutInflater;
    private RoundedTransformation mBlueTransformation;
    private RoundedTransformation mRedTransform;
    Picasso picasso;

    public PeopleListAdapter(Context context) {
        super(context, R.layout.profile_list_item, R.id.profile_list_item_name);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(context);
        Resources res = context.getResources();
        mBlueTransformation = new RoundedTransformation(100, 5,
                res.getColor(R.color.hackillinois_blue),
                res.getInteger(R.integer.people_image_border_width));
        mRedTransform = new RoundedTransformation(100, 5,
                res.getColor(R.color.hackillinois_red),
                res.getInteger(R.integer.people_image_border_width));
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
            String url = "https://graph.facebook.com/" + person.getFbID() + "/picture?type=large";
            RequestCreator requestCreator = picasso.load(url).resize(200, 200);

            viewHolder.nameTextView.setText(person.getName());
            if (person instanceof Staff ) {
                requestCreator = requestCreator.transform(mRedTransform);
                viewHolder.companyTextView.setText(((Staff) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Staff) person).getJobTitle());
            } else if (person instanceof Mentor) {
                requestCreator = requestCreator.transform(mRedTransform);
                viewHolder.companyTextView.setText(((Mentor) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Mentor) person).getJobTitle());
            } else if (person instanceof Hacker) {
                requestCreator = requestCreator.transform(mBlueTransformation);
                viewHolder.companyTextView.setText(((Hacker) person).getSchool());
                viewHolder.jobTitleTextView.setText(((Hacker) person).getYear());
            }
            requestCreator.centerCrop().into(viewHolder.profileImageView);
            viewHolder.locationTextView.setText(person.getHomebase());
        }
        return convertView;
    }

    public void setData(List<? extends Person> data) {
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
