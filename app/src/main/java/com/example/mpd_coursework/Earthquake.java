package com.example.mpd_coursework;
/*
    Name : Connor Goodman
    ID : S1625004
 */
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake implements Serializable {

    private String title;
    private String description;
    private String location;
    private String link;
    private String date;
    private Date dateTime;
    private String category;
    private String geoLatString;
    private String geoLongString;
    private float geoLat;
    private float geoLong;
    private float magnitude;
    private int depth;
    private String magColour;




    public Earthquake()
    {
        title = "";
        description = "";
        link = "";
        date = "";
        category = "";
        geoLatString = "";
        geoLongString = "";
    }

    public Earthquake(String nTitle, String nDescription, String nLink, String nDate, String nCatagory, String nLat, String nLong)
    {
        title = nTitle;
        description = nDescription;
        link = nLink;
        date = nDate;
        category = nCatagory;
        geoLatString = nLat;
        geoLongString = nLong;
    }

    //Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getLink() { return link; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getLatString() { return geoLatString; }
    public String getLongString() { return geoLongString; }
    public float getGeoLat() { return geoLat; }
    public float getGetLong() { return geoLong; }
    public float getMagnitude() { return magnitude; }
    public int getDepth() { return depth; }
    public String getMagColour() { return magColour; }
    public Date getDateTime() {return dateTime; }






    //Setters
    public void setTitle(String nString) { title = nString; }
    public void setDescription(String nString) { description = nString; }
    public void setLocation(String nString) { location = nString; }
    public void setLink(String nString) { link = nString; }
    public void setDate(String nString) { date = nString; setDateTime(nString); }
    public void setCategory(String nString) { category = nString; }
    public void setLatString(String nString) { geoLatString = nString; }
    public void setLongString(String nString) { geoLongString = nString; }
    public void setGeoLat(float nFloat) { geoLat = nFloat; }
    public void setGeoLong(float nFloat) { geoLong = nFloat; }
    public void setMagnitude(float nFloat) { magnitude = nFloat; setMagColour(nFloat); }
    public void setDepth(int nInt) { depth = nInt; }
    public void setMagColour(float mag)
    {
        if(mag > 9.5)
        {
            magColour = "#fa0000";
        }
        else if(mag > 9.0)
        {
            magColour = "#fa2600";
        }
        else if(mag > 8.5)
        {
            magColour = "#fa3e00";
        }
        else if(mag > 8.0)
        {
            magColour = "#fa5300";
        }
        else if(mag > 7.5)
        {
            magColour = "#fa6000";
        }
        else if(mag > 7.0)
        {
            magColour = "#fa7900";
        }
        else if(mag > 6.5)
        {
            magColour = "#fa8a00";
        }
        else if(mag > 6.0)
        {
            magColour = "#fa9a00";
        }
        else if(mag > 5.5)
        {
            magColour = "#faab00";
        }
        else if(mag > 5.0)
        {
            magColour = "#facc00";
        }
        else if(mag > 4.5)
        {
            magColour = "#fae500";
        }
        else if(mag > 4.0)
        {
            magColour = "#f2fa00";
        }
        else if(mag > 3.5)
        {
            magColour = "#c4fa00";
        }
        else if(mag > 3.0)
        {
            magColour = "#c4fa00";
        }
        else if(mag > 2.5)
        {
            magColour = "#affa00";
        }
        else if(mag > 2)
        {
            magColour = "#96fa00";
        }
        else if(mag > 1.5)
        {
            magColour = "#81fa00";
        }
        else if(mag > 1.0)
        {
            magColour = "#75fa00";
        }
        else if(mag > 0.5)
        {
            magColour = "#58fa00";
        }
        else if(mag > 0)
        {
            magColour = "#36fa00";
        }
    }

    public void setDateTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }




}
