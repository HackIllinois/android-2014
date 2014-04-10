package org.hackillinois.android.people;

import android.util.SparseArray;

import org.hackillinois.android.models.people.Hacker;
import org.hackillinois.android.models.people.Mentor;
import org.hackillinois.android.models.people.Person;

import java.util.HashMap;
import java.util.List;

public class PeopleDataHolder {

    private List<Mentor> mentorAndStaffList;
    private List<Hacker> hackerList;
    private SparseArray<Person> iOSMap = new SparseArray<Person>();
    private HashMap<String, Person> androidMap = new HashMap<String, Person>();

    public List<Mentor> getMentorAndStaffList() {
        return mentorAndStaffList;
    }

    public void setMentorAndStaffList(List<Mentor> mentorAndStaffList) {
        this.mentorAndStaffList = mentorAndStaffList;
    }

    public List<Hacker> getHackerList() {
        return hackerList;
    }

    public void setHackerList(List<Hacker> hackerList) {
        this.hackerList = hackerList;
    }

    public SparseArray<Person> getiOSMap() {
        return iOSMap;
    }

    public void setiOSMap(SparseArray<Person> iOSMap) {
        this.iOSMap = iOSMap;
    }

    public HashMap<String, Person> getAndroidMap() {
        return androidMap;
    }

    public void setAndroidMap(HashMap<String, Person> androidMap) {
        this.androidMap = androidMap;
    }
}
