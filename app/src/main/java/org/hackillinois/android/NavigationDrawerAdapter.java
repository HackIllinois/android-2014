package org.hackillinois.android;
import android.content.Context;
import android.graphics.Color;
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
    int[] deselectedIcons = {R.drawable.ic_person_selected, R.drawable.ic_people_tab_deselected, R.drawable.ic_feed_tab_deselected, R.drawable.ic_schedule_tab_deselected, R.drawable.ic_support_tab_deselected};


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

            if (position == 0) {
                viewHolder.textView.setTextColor(Color.WHITE);
            }

            convertView.setTag(viewHolder);

            viewHolder.textView.setText(values[position]);
            viewHolder.iconImageView.setImageResource(deselectedIcons[position]);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        public ImageView iconImageView;
        public TextView textView;
    }
}
