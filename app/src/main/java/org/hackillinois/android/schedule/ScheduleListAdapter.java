package org.hackillinois.android.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hackillinois.android.R;
import org.hackillinois.android.models.ScheduleItem;

import java.util.Calendar;
import java.util.List;

/**
 * @author vishal
 */
public class ScheduleListAdapter extends ArrayAdapter<ScheduleItem> {

    private LayoutInflater mInflater;
    private Picasso picasso;
    private RoundedTransformation mRoundedTransformation;


    /**
     * Constructor
     *
     * @param context The current context.
     */
    public ScheduleListAdapter(Context context) {
        super(context, R.layout.schedule_list_item, R.id.schedule_description);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(context);
        mRoundedTransformation = new RoundedTransformation(100, 20, 0, 0);
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
            holder.calendarTextView = (TextView) rowView.findViewById(R.id.schedule_add_calendar);
            holder.calendarImageView = (ImageView) rowView.findViewById(R.id.schedule_add_calendar_icon);

            rowView.setTag(holder); // set the tag for this row so that it can be retrieved again
        } else {
            holder = (ViewHolder) rowView.getTag(); // re-use a row view that is out of sight
        }

        // load the views in the row with data
        final ScheduleItem item = getItem(position);
        holder.timeTextView.setText(item.getTime());
        // load the image into the ImageView
        if(item.getIconURL() != null)
            picasso.load(item.getIconURL()).resize(200, 200).transform(mRoundedTransformation).centerCrop().into(holder.iconImageView);

        holder.titleTextView.setText(item.getEventName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.roomTextView.setText(item.getRoomName() + " " + item.getRoomNumber());
        holder.calendarTextView.setText("Add");

        final View.OnClickListener addToCalendar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();

            }
        };

        holder.calendarTextView.setOnClickListener(addToCalendar);
        holder.calendarImageView.setOnClickListener(addToCalendar);

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
        public TextView calendarTextView;
        public ImageView calendarImageView;
    }

}
