package org.hackillinois.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Will Hennessy
 *  Simple model class to represent a user skill.
 */
public class Skill {

    private String name;
    private ArrayList<String> tags;
    private ArrayList<String> aliases;
    private boolean isSelected;

    public Skill(JSONObject skill) {
        try {
            this.name = skill.getString("name");

            this.tags = new ArrayList<String>();
            JSONArray jsonTags = skill.getJSONArray("tags");
            for (int i = 0; i < jsonTags.length(); i++)
                tags.add( jsonTags.getString(i) );

            this.aliases = new ArrayList<String>();
            JSONArray jsonAliases = skill.getJSONArray("alias");
            for (int i = 0; i < jsonAliases.length(); i++)
                tags.add( jsonAliases.getString(i) );

            this.isSelected = false;  // will be set later by the SkillsDialogFragment

        } catch(JSONException j) {
            j.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<String> aliases) {
        this.aliases = aliases;
    }

    /** Given a string constraint, determine if this skill matches the constraint **/
    public boolean isMatch(String constraint) {
        // if the constraint is a prefix of the name
        if(this.getName().toLowerCase().startsWith(constraint))
            return true;

        /** Note:  here we use contains() instead of startsWith() because of a bug in the backend.
         *  Many (but not all) tags and aliases contain an extra space at the front.
         *  i.e. " Backend" instead of "Backend"
         *  Next year, fix that in the backend and use the more efficient startsWith() here. */

        for(String tag : tags) {
            if (tag.toLowerCase().contains(constraint)) //startsWith(constraint))
                return true;
        }

        // if the constraint is a prefix of an alias
        for(String alias : aliases)
            if(alias.toLowerCase().contains(constraint)) //startsWith(constraint))
                return true;

        return false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}