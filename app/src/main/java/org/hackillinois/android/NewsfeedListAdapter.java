package org.hackillinois.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    Picasso picasso;

    /** Constructor **/
    public NewsfeedListAdapter(Activity activity) {
        super(activity, R.layout.newsfeed_list_item, R.id.newsfeed_list_item_description);
        mLayoutInflater = activity.getLayoutInflater();
        picasso = Picasso.with(activity);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if(rowView == null) { // try to reuse a row view that is out of sight
            rowView = mLayoutInflater.inflate(R.layout.newsfeed_list_item, null);

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

        picasso.load(newsItem.getIconUrl())
                // waiting for Eva to reply with more info about theses images before I do formatting...
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                //.centerInside()
                .into(holder.image);

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
