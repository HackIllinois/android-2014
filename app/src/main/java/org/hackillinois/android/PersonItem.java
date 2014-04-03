package org.hackillinois.android;

/**
 * Created by Stephen on 3/31/14.
 */
public class PersonItem
{
    private String name;
    private String company;
    private String job_title;
    private String location;
    private String pictureURL;

    public PersonItem(String name, String company, String job_title,
                      String location, String pictureURL){
        this.name = name;
        this.company = company;
        this.job_title = job_title;
        this.location = location;
        this.pictureURL = pictureURL;
    }

    public String getName()
    {
        return name;
    }

    public String getCompany()
    {
        return company;
    }

    public String getJobTitle()
    {
        return job_title;
    }

    public String getLocation()
    {
        return location;
    }

    public String getPictureURL()
    {
        return pictureURL;
    }
}
