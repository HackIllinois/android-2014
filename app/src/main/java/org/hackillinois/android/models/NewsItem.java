package org.hackillinois.android.models;

import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;

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


    /** Getters and Setters **/
    public Spanned getDescription() {
        return Html.fromHtml(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
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

}
