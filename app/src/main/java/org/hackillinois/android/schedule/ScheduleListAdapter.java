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
        mActivity = activity;
        mRoundedTransformation = new RoundedTransformation(100, 20, 0, 0);
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
        if (item.getIconURL() != null)
            picasso.load(item.getIconURL()).resize(200, 200).transform(mRoundedTransformation).centerCrop().into(holder.iconImageView);

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
                String room = item.getRoomNumber();
                double lat = 0;
                double lng = 0;
                if (room.equals("SC 0216")) {
                    lat = 40.113760;
                    lng = -88.225017;
                } else if (room.equals("SC 0224")) {
                    lat = 40.113772;
                    lng = -88.224636;
                } else if (room.equals("SC Basement")) {
                    lat = 40.113870;
                    lng = -88.224848;
                } else if (room.equals("SC 1103")) {
                    lat = 40.113964;
                    lng = -88.225296;
                } else if (room.equals("SC 1105")) {
                    lat = 40.114066;
                    lng = -88.225294;
                } else if (room.equals("SC 1109")) {
                    lat = 40.114167;
                    lng = -88.225295;
                } else if (room.equals("SC 1112")) {
                    lat = 40.114301;
                    lng = -88.225133;
                } else if (room.equals("SC 1131")) {
                    lat = 40.114276;
                    lng = -88.225307;
                } else if (room.equals("SC 1214")) {
                    lat = 40.113791;
                    lng = -88.224911;
                } else if (room.equals("SC 1302")) {
                    lat = 40.113794;
                    lng = -88.224741;
                } else if (room.equals("SC 1304")) {
                    lat = 40.113793;
                    lng = -88.224603;
                } else if (room.equals("SC 1404")) {
                    lat = 40.114056;
                    lng = -88.224460;
                } else if (room.equals("SC Atrium")) {
                    lat = 40.113886;
                    lng = -88.224955;
                } else if (room.equals("SC 1312")) {
                    lat = 40.113719;
                    lng = -88.224829;
                } else if (room.equals("SC 2405")) {
                    lat = 40.113990;
                    lng = -88.224428;
                } else if (room.equals("SC 2102")) {
                    lat = 40.113928;
                    lng = -88.225188;
                } else if (room.equals("SC 2124")) {
                    lat = 40.114299;
                    lng = -88.225139;
                } else if (room.equals("SC 3401")) {
                    lat = 40.113976;
                    lng = -88.224555;
                } else if (room.equals("SC 3403")) {
                    lat = 40.113986;
                    lng = -88.224469;
                } else if (room.equals("SC 3405")) {
                    lat = 40.113994;
                    lng = -88.224380;
                } else if (room.equals("SC 3102")) {
                    lat = 40.113925;
                    lng = -88.225189;
                } else if (room.equals("SC 3124")) {
                    lat = 40.114298;
                    lng = -88.225131;
                } else if (room.equals("SC 4102")) {
                    lat = 40.113929;
                    lng = -88.225190;
                } else if (room.equals("SC 4124")) {
                    lat = 40.114301;
                    lng = -88.225132;
                } else if (room.equals("SC 4403")) {
                    lat = 40.114001;
                    lng = -88.224473;
                } else if (room.equals("SC 4405")) {
                    lat = 40.114001;
                    lng = -88.224383;
                } else if (room.equals("SC 4407")) {
                    lat = 40.114014;
                    lng = -88.224293;
                } else if (room.equals("DCL 1310")) {
                    lat = 40.113185;
                    lng = -88.225861;
                } else if (room.equals("DCL 1320")) {
                    lat = 40.113210;
                    lng = -88.226039;
                } else if (room.equals("DCL 2320")) {
                    lat = 40.113228;
                    lng = -88.226011;
                } else if (room.equals("DCL 2436")) {
                    lat = 40.113116;
                    lng = -88.226112;
                } else {
                    lat = 40.114300;
                    lng = -88.224837;
                }

                String uriBegin = "geo:" + lat + "," + lng;
                String query = lat + "," + lng + "(" + item.getEventName() + " " + room + ")";
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
