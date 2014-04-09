package org.hackillinois.android;
import android.content.Context;
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];
        if(s.equals("Profile")) {
            imageView.setImageResource(R.drawable.ic_person150);
        }
        if(s.equals("People")) {
            imageView.setImageResource(R.drawable.ic_people_tab_deselected150);
        }
        if(s.equals("Feed")) {
            imageView.setImageResource(R.drawable.ic_feed_tab_deselected150);
        }
        if(s.equals("Schedule")) {
            imageView.setImageResource(R.drawable.ic_schedule_tab_deselected150);
        }

        return rowView;
    }
}
