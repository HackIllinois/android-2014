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
            viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.profile_image);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.profile_list_item_name);
            viewHolder.companyTextView = (TextView) convertView.findViewById(R.id.profile_list_item_company);
            viewHolder.jobTitleTextView = (TextView) convertView.findViewById(R.id.profile_list_item_job_title);
            viewHolder.locationTextView = (TextView) convertView.findViewById(R.id.profile_list_item_job_title);
            viewHolder.initialsTextView = (TextView) convertView.findViewById(R.id.profile_initials);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Person person = getItem(position);
        if (person != null) {

            viewHolder.nameTextView.setText(person.getName());
            viewHolder.locationTextView.setText(person.getHomebase());

            RequestCreator requestCreator;

            if (!person.getFbID().isEmpty()) {
                requestCreator = picasso.load(person.getImageURL()).resize(200, 200).centerCrop();
                viewHolder.initialsTextView.setVisibility(View.INVISIBLE);
                viewHolder.profileImageView.setVisibility(View.VISIBLE);
            } else{ // put in initials for the image view
                requestCreator = picasso.load("http://localhost").resize(200, 200).centerCrop().error(R.drawable.ic_action_hackillinois_icon_white).placeholder(R.drawable.ic_action_hackillinois_icon_white);
                viewHolder.profileImageView.setVisibility(View.VISIBLE);
                viewHolder.initialsTextView.setVisibility(View.VISIBLE);
                String parseThisShit = person.getName();
                int space = parseThisShit.indexOf(" ");
                String firstName = parseThisShit.substring(0, 1);
                String lastName = parseThisShit.substring(space + 1, space + 2);
                String initials = firstName.toUpperCase() + lastName.toUpperCase();
                viewHolder.initialsTextView.setText(initials);
            }
            if (person instanceof Staff) {
                requestCreator.transform(mRedTransform);
                viewHolder.companyTextView.setText(((Staff) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Staff) person).getJobTitle());
                viewHolder.initialsTextView.setTextColor(getContext().getResources().getColor(R.color.hackillinois_red));
            } else if (person instanceof Mentor) {
                requestCreator.transform(mRedTransform);
                viewHolder.companyTextView.setText(((Mentor) person).getCompany());
                viewHolder.jobTitleTextView.setText(((Mentor) person).getJobTitle());
                viewHolder.initialsTextView.setTextColor(getContext().getResources().getColor(R.color.hackillinois_red));
            } else if (person instanceof Hacker) {
                requestCreator.transform(mBlueTransformation);
                viewHolder.companyTextView.setText(((Hacker) person).getSchool());
                viewHolder.jobTitleTextView.setText(((Hacker) person).getYear());
                viewHolder.initialsTextView.setTextColor(getContext().getResources().getColor(R.color.hackillinois_blue));
            }
            requestCreator.into(viewHolder.profileImageView);
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
        public TextView initialsTextView;
    }
}
