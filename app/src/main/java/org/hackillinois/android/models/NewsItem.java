package org.hackillinois.android.models;

import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

/**
 * @author Will Hennessy
 *
 * Simple class to represent an item in the newsfeed.
 */
public class NewsItem {

    private String description;
    private int time;
    private String iconUrl;
    private ArrayList<Highlight> highlights;  // [[range,color], [range,color]]
    private boolean isEmergency;


    /** Constructor **/
    public NewsItem(String description, int time, String iconUrl, boolean isEmergency) {
        this.description = description;
        this.time = time;
        this.iconUrl = iconUrl;
        this.isEmergency = isEmergency;
        this.highlights = null;
    }


    /** ----- Getters and Setters ----- **/
    public Spanned getDescription() {
        return Html.fromHtml(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return format_time(time);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }


    /** ----- Private functions ----- **/
    /** Convert an int unix time to a String i.e. '5m ago' **/
    private String format_time(int unixTime) {
        DateTime time = new DateTime((long) unixTime*1000);
        DateTime now = new DateTime();
        Period diff = new Period(time, now);

        if(diff.getDays() > 0)
            return diff.getDays() + "d ago";
        else if(diff.getHours() > 0)
            return diff.getHours() + "h ago";
        else
            return diff.getMinutes() + "m ago";
    }


    /** Private class to represent colored words in the news description **/
    private class Highlight {
        public Highlight(int startIdx, int endIdx, int r, int g, int b) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.color = String.format("#%06X", 0xFFFFFF & Color.rgb(r, g, b));
        }

        int startIdx;       // index of the first char in the string to highlight
        int endIdx;         // index of the last char in the string to highlight
        String color;       // String hex color to highlight the text
    }


    public void addHighlight(int startIdx, int endIdx, int r, int g, int b) {
        if (highlights == null)
            highlights = new ArrayList<Highlight>();

        highlights.add(new Highlight(startIdx, endIdx, r, g, b));
        generateHighlightedDescription();   // regenerate a description with colored word
    }


    /** Private method to generate a String description with colored/highlighted words
     *  and set that as the member variable description. */
    private void generateHighlightedDescription() {
        StringBuilder colored = new StringBuilder();
        Highlight currHighlight = null;

        for( int i = 0; i < description.length(); i++ ) {

            // check if this index is the start of a highlighted region
            for(Highlight h : highlights) {
                if ( currHighlight == null && i == h.startIdx ) {
                    colored.append("<font color='");
                    colored.append(h.color);
                    colored.append("'>");
                    currHighlight = h;
                }
            }

            // append the current letter
            colored.append(description.charAt(i));

            // check if this index is the end of a highlighted region
            if( currHighlight != null && i == currHighlight.endIdx ) {
                colored.append("</font>");
                currHighlight = null;
            }
        }

        this.description = colored.toString();
    }

}
