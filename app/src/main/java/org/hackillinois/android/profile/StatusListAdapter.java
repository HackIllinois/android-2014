package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Status;

/**
 * Created by Jonathan on 4/9/14.
 */
public class StatusListAdapter extends ArrayAdapter<Status> {


    private LayoutInflater mLayoutInflater;


    public StatusListAdapter(Activity activity) {
        super(activity, R.layout.status_list_item, R.id.status_time);
        mLayoutInflater = activity.getLayoutInflater();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = mLayoutInflater.inflate(R.layout.status_list_item, parent, false);

        TextView textView0 = (TextView) rowView.findViewById(R.id.status_time);
        TextView textView1 = (TextView) rowView.findViewById(R.id.status_text);

        Status status = getItem(position);

        textView0.setText("" + status.getDate());
        textView1.setText(status.getStatus());

        return rowView;
    }

}
