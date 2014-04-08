package org.hackillinois.android.news;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.models.NewsItem;

import java.util.List;

/**
 * @author Will Hennessy
 *
 *  ArrayAdapter class to back the Newsfeed list view.
 *  Uses lazy loading ViewHolders to optimize scrolling efficiency.
 */
public class NewsfeedListAdapter extends ArrayAdapter<NewsItem> {

    private LayoutInflater mLayoutInflater;

    /** Constructor **/
    public NewsfeedListAdapter(Activity activity) {
        super(activity, R.layout.newsfeed_list_item, R.id.newsfeed_list_item_description);
        mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.newsfeed_list_item, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) rowView.findViewById(R.id.newsfeed_list_item_image);
            holder.description = (TextView) rowView.findViewById(R.id.newsfeed_list_item_description);
            holder.time = (TextView) rowView.findViewById(R.id.newsfeed_list_item_time);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        NewsItem newsItem = getItem(position);

        holder.description.setText( newsItem.getDescription() );
        holder.time.setText( newsItem.getTime() );
        final Resources resources = getContext().getResources();
        // this is super hacky
        if (newsItem.isEmergency()) {
            holder.image.setImageDrawable(resources.getDrawable(R.drawable.emergency));
        } else if (newsItem.getIconUrl().contains("announce")){
            holder.image.setImageDrawable(resources.getDrawable(R.drawable.announce));
        } else {
            holder.image.setImageDrawable(resources.getDrawable(R.drawable.hackillinois));
        }
        return rowView;
    }


    /** Set the data for the list adapter **/
    public void setData(List<NewsItem> data) {
        if (data != null) {
            clear();
            for (NewsItem news : data) {
                add(news);
            }
        }
    }


    /** Custom class to hold rowViews in memory and re-use them
     *  Decreases number of calls to rowView.findViewById() which is expensive. **/
    static class ViewHolder {
        public ImageView image;
        public TextView description;
        public TextView time;
    }

}
