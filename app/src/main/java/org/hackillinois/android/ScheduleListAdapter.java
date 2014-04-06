package org.hackillinois.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hackillinois.android.models.ScheduleItem;

import java.util.List;

/**
 * @author vishal
 */
public class ScheduleListAdapter extends ArrayAdapter<ScheduleItem> {

    private LayoutInflater mInflater;
    private Picasso picasso;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public ScheduleListAdapter(Context context) {
        super(context, R.layout.schedule_list_item, R.id.schedule_description);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(context);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;

        if (rowView == null) {
            //create a brand new row
            rowView = mInflater.inflate(R.layout.schedule_list_item, parent, false);

            holder = new ViewHolder();
            holder.timeTextView = (TextView) rowView.findViewById(R.id.schedule_time);
            holder.iconImageView = (ImageView) rowView.findViewById(R.id.schedule_icon);
            holder.titleTextView = (TextView) rowView.findViewById(R.id.schedule_title);
            holder.descriptionTextView = (TextView) rowView.findViewById(R.id.schedule_description);
            holder.roomTextView = (TextView) rowView.findViewById(R.id.schedule_room);

            rowView.setTag(holder); // set the tag for this row so that it can be retrieved again
        } else {
            holder = (ViewHolder) rowView.getTag(); // re-use a row view that is out of sight
        }

        // load the views in the row with data
        ScheduleItem item = getItem(position);
        holder.timeTextView.setText(Integer.toString(item.getTime()));
        // load the image into the ImageView
        if(item.getIconURL() != null)
            picasso.load(item.getIconURL()).into(holder.iconImageView);

        holder.titleTextView.setText(item.getEventName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.roomTextView.setText(item.getRoomName() + " " + item.getRoomNumber());

        return rowView;
    }

    /**
     * Makes a list of all the rows
     *
     * @param dataList data
     */
    public void setData(List<ScheduleItem> dataList) {
        if (dataList != null) {
            clear();
            for (ScheduleItem item : dataList) {
                add(item); // add each item to the end of the list
            }
        }
    }

    /**
     * The views that are in one row of the list adapter
     */
    static class ViewHolder {
        public TextView timeTextView;
        public ImageView iconImageView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView roomTextView;
    }

}