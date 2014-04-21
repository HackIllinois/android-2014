package org.hackillinois.android.models;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by fishbeinb on 4/20/2014.
 */
public class Support implements Serializable {
    private TreeMap<String, List<String>> supportCategories;

    public Support() {
        this.supportCategories = new TreeMap<String, List<String>>();
    }

    public Support(TreeMap<String, List<String>> Categories) {
        this.supportCategories = Categories;
    }

    public TreeMap<String, List<String>> getCategories() {
        return supportCategories;
    }

    public void setCategories(TreeMap<String, List<String>> Categories) {
        this.supportCategories = Categories;
    }

    public void addCategory(String category, List<String> subCategoryList) {
        supportCategories.put(category, subCategoryList);
    }

}
