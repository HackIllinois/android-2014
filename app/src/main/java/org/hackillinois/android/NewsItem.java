package org.hackillinois.android;

import android.graphics.Color;

/**
 * @author Will Hennessy
 *
 * Simple class to represent an item in the newsfeed.
 */
public class NewsItem {

    private String subject;
    private String description;
    private String time;
    private String iconUrl;
    private Highlight[] highlights;  //:[[range,color,type]],
    private boolean isEmergency;


    /** Constructor **/
    public NewsItem(String subject, String description, String time, String iconUrl, Highlight[] highlighted, boolean isEmergency) {
        this.subject = subject;
        this.description = description;
        this.time = time;
        this.iconUrl = iconUrl;
        this.highlights = highlighted;
        this.isEmergency = isEmergency;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Highlight[] getHighlights() {
        return highlights;
    }

    public void setHighlights(Highlight[] highlights) {
        this.highlights = highlights;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }


    private class Highlight {
        public Highlight(int startIdx, int endIdx, int r, int g, int b) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.color = Color.rgb(r, g, b);
        }

        int startIdx;       // index of the first char in the string to highlight
        int endIdx;         // index of the last char in the string to highlight
        int color;          // integer rgb color to highlight the text
    }


}
