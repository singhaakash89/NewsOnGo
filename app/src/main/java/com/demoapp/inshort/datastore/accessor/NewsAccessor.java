package com.demoapp.inshort.datastore.accessor;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class NewsAccessor {
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String CATEGORY = "CATEGORY";
    public static final String HOSTNAME = "HOSTNAME";
    public static final String TIMESTAMP = "TIMESTAMP";

    public static String getID() {
        return ID;
    }


    public static String getTITLE() {
        return TITLE;
    }

    public static String getURL() {
        return URL;
    }

    public static String getPUBLISHER() {
        return PUBLISHER;
    }

    public static String getCATEGORY() {
        return CATEGORY;
    }

    public static String getHOSTNAME() {
        return HOSTNAME;
    }

    public static String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public static String[] getTableProjection() {
        String[] projection = new String[]{
                getID(),
                getTITLE(),
                getURL(),
                getPUBLISHER(),
                getCATEGORY(),
                getHOSTNAME(),
                getTIMESTAMP()};
        return projection;
    }

}
