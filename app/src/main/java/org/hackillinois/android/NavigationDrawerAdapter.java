package org.hackillinois.android;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by Jonathan on 4/8/14.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final String[] values;

    public NavigationDrawerAdapter(Context context, String[] values) {
        super(context, R.layout.drawer_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.icon_drawer);
            if (position == 0) { viewHolder.textView.setTypeface(null, Typeface.BOLD); }
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(values[position]);

        String s = values[position];
        if(s.equals(context.getString(R.string.title_section1))) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_person150_1);
        }
        if(s.equals(context.getString(R.string.title_section2))) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_people_tab_delected150);
        }
        if(s.equals(context.getString(R.string.title_section3))) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_feed_tab_delected150);
        }
        if(s.equals(context.getString(R.string.title_section4))) {
            viewHolder.iconImageView.setImageResource(R.drawable.ic_schedule_tab_delected150);
        }


        return convertView;
    }

    static class ViewHolder {
        public ImageView iconImageView;
        public TextView textView;
    }
}
