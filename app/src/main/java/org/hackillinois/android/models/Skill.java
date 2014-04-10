package org.hackillinois.android.models;

import java.util.ArrayList;

/**
 * @author Will Hennessy
 *  Simple model class to represent a user skill.
 */
public class Skill {

    private String name;
    private ArrayList<String> tags;
    private ArrayList<String> aliases;

    public Skill(String name, ArrayList<String> tags, ArrayList<String> aliases) {
        this.name = name;
        this.tags = tags;
        this.aliases = aliases;
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
}
