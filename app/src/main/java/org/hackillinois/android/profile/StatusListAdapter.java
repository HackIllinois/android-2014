package org.hackillinois.android.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Status;

import java.util.List;

/**
 * Created by Jonathan on 4/9/14.
 */
public class StatusListAdapter extends ArrayAdapter<Status> {

    private LayoutInflater mLayoutInflater;

    /** Constructor **/
    public StatusListAdapter(Activity activity) {
        super(activity, R.layout.status_list_item, R.id.status_time);
        mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.status_list_item, parent, false);

            holder = new ViewHolder();
            holder.time = (TextView) rowView.findViewById(R.id.status_time);
            holder.text = (TextView) rowView.findViewById(R.id.status_text);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Status status = getItem(position);
        if(status!=null) {
            long unixTime = System.currentTimeMillis()/1000L;
            int minutes = (int)(unixTime-status.getDate())/(1000*60);
            holder.time.setText("" + minutes + "m");
            holder.text.setText(status.getStatus());
        }
        return rowView;
    }


    /** Set the data for the list adapter **/
    public void setData(List<Status> data) {
        if (data != null) {
            clear();
            for (Status status : data) {
                add(status);
            }
        }
    }


    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public TextView text;
        public TextView time;
    }
}
