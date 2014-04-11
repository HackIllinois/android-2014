package org.hackillinois.android.models.Support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fishbeinb on 4/11/14.
 */
public class SupportData {

    private List<Support> Rooms = new ArrayList<Support>();
    private List<Support> Categories = new ArrayList<Support>();
    private Map<Support, List<Support>> SubCategories = new HashMap<Support, List<Support>>();

    public SupportData(List<Support> Rooms, List<Support> Categories,  Map<Support, List<Support>> SubCategories) {
        this.Rooms = Rooms;
        this.Categories = Categories;
        this.SubCategories = SubCategories;
    }

    public List<Support> getRooms() {
        return Rooms;
    }

    public List<Support> getCategories() {
        return Categories;
    }

    public Map<Support, List<Support>> getSubCategories() {
        return SubCategories;
    }

    public void setRooms(List<Support> Rooms) {
        this.Rooms = Rooms;
    }

    public void setCategories(List<Support> setCategories) {
        this.Categories = Categories;
    }

    public void setSubCategories(Map<Support, List<Support>> Categories) {
        this.SubCategories = SubCategories;
    }
}
