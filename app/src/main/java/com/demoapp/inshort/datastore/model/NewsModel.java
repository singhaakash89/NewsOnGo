package com.demoapp.inshort.datastore.model;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.demoapp.inshort.datastore.accessor.NewsAccessor;

import java.sql.Date;

/**
 * Created by aakashsingh on 16/09/17.
 */

public class NewsModel implements Comparable {

    private Integer ID;
    private String TITLE;
    private String URL;
    private String PUBLISHER;
    private String CATEGORY;
    private String HOSTNAME;
    private long TIMESTAMP;
    private Date date;

    public NewsModel() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPUBLISHER() {
        return PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getHOSTNAME() {
        return HOSTNAME;
    }

    public void setHOSTNAME(String HOSTNAME) {
        this.HOSTNAME = HOSTNAME;
    }

    public long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ContentValues getContentValues() {
        ContentValues row = new ContentValues();
        row.put(NewsAccessor.ID, getID());
        row.put(NewsAccessor.TITLE, getTITLE());
        row.put(NewsAccessor.URL, getURL());
        row.put(NewsAccessor.PUBLISHER, getPUBLISHER());
        row.put(NewsAccessor.CATEGORY, getCATEGORY());
        row.put(NewsAccessor.HOSTNAME, getHOSTNAME());
        row.put(NewsAccessor.TIMESTAMP, getTIMESTAMP());
        return row;
    }

    @Override
    public int compareTo(@NonNull Object anotherObj) {
        //for Long
        return Long.compare(this.TIMESTAMP, ((NewsModel) anotherObj).TIMESTAMP);
    }
}
