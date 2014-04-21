package org.hackillinois.android.support;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.hackillinois.android.R;

import java.util.List;

/**
 * @author Will Hennessy
 *
 *  ArrayAdapter class to back the Newsfeed list view.
 *  Uses lazy loading ViewHolders to optimize scrolling efficiency.
 */
public class SupportListAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;

    public SupportListAdapter(Activity activity) {
        super(activity, R.layout.support_list_item, R.id.support_list_item_title);
        mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.support_list_item, parent, false);

            holder = new ViewHolder();
            assert rowView != null;
            holder.name = (TextView) rowView.findViewById(R.id.support_list_item_title);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        String currentCategoy = getItem(position);

        holder.name.setText( currentCategoy );
        return rowView;
    }


    /** Set the data for the list adapter **/
    public void setData(List<String> data) {
        if (data != null) {
            clear();
            for (String title : data) {
                add(title);
            }
        }
    }

    /** Set the data for the list adapter **/
    public int getSize() {
        return this.getCount();
    }

    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public TextView name;
    }

}
