package org.hackillinois.android.schedule;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hackillinois.android.R;
import org.hackillinois.android.RoundedTransformation;
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
    private Activity mActivity;

    public ScheduleListAdapter(Activity activity) {
        super(activity, R.layout.schedule_list_item, R.id.schedule_description);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        picasso = Picasso.with(activity);
        mRoundedTransformation = new RoundedTransformation(100, 20, 0, 0);
        mActivity = activity;
    }

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
            holder.roomImageView = (ImageView) rowView.findViewById(R.id.schedule_loc_icon);
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
        if (item.getIconURL() != null && !item.getIconURL().isEmpty()) {
            holder.iconImageView.setImageResource(item.getImageDrawable());
        } else {
            picasso.load(item.getIconURL()).resize(200, 200).transform(mRoundedTransformation).centerCrop().into(holder.iconImageView);
        }

        holder.titleTextView.setText(item.getEventName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.roomTextView.setText(item.getRoomNumber());
        holder.calendarTextView.setText("Add");

        final View.OnClickListener addToCalendar = new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2014, Calendar.APRIL, item.getDay(), item.getHour(), item.getMinute());
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, item.getEventName())
                        .putExtra(CalendarContract.Events.DESCRIPTION, item.getDescription())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, item.getRoomName() + " " + item.getRoomNumber())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                mActivity.startActivityForResult(intent, 0);
            }
        };

        final View.OnClickListener googleMaps = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = item.getRoomLat();
                double lng = item.getRoomLong();

                String uriBegin = "geo:" + lat + "," + lng;
                String query = lat + "," + lng + "(" + item.getEventName() + " in " + item.getRoomName() + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=19";
                Uri uri = Uri.parse(uriString);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(intent);
            }
        };

        holder.calendarTextView.setOnClickListener(addToCalendar);
        holder.calendarImageView.setOnClickListener(addToCalendar);

        holder.roomTextView.setOnClickListener(googleMaps);
        holder.roomImageView.setOnClickListener(googleMaps);
        return rowView;
    }

    public void setData(List<ScheduleItem> dataList) {
        if (dataList != null) {
            clear();
            for (ScheduleItem item : dataList) {
                add(item); // add each item to the end of the list
            }
        }
    }

    static class ViewHolder {
        public TextView timeTextView;
        public ImageView iconImageView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView roomTextView;
        public TextView calendarTextView;
        public ImageView calendarImageView;
        public ImageView roomImageView;
    }

}
