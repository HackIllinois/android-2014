package org.hackillinois.android.support;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.Support.Support;

import java.util.List;

/**
 * @author Will Hennessy
 *
 *  ArrayAdapter class to back the Newsfeed list view.
 *  Uses lazy loading ViewHolders to optimize scrolling efficiency.
 */
public class SupportListAdapter extends ArrayAdapter<Support> {

    private LayoutInflater mLayoutInflater;

    /** Constructor **/
    public SupportListAdapter(Activity activity) {
        super(activity, R.layout.support_item, R.id.support_item_title);
        mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.support_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.support_item_title);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Support Support = getItem(position);

        holder.name.setText( Support.getTitle() );

        return rowView;
    }


    /** Set the data for the list adapter **/
    public void setData(List<Support> data) {
        if (data != null) {
            clear();
            for (Support title : data) {
                add(title);
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
