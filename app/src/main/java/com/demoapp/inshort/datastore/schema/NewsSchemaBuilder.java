package com.demoapp.inshort.datastore.schema;


import com.demoapp.inshort.datastore.accessor.NewsAccessor;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class NewsSchemaBuilder {

    public static final String TABLE_NAME = "news_table";

    public static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" + NewsAccessor.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NewsAccessor.TITLE + " VARCHAR(255) , " +
            NewsAccessor.URL + " VARCHAR(255) , " +
            NewsAccessor.PUBLISHER + " INTEGER  , " +
            NewsAccessor.CATEGORY + " INTEGER  ," +
            NewsAccessor.HOSTNAME + " VARCHAR  ," +
            NewsAccessor.TIMESTAMP + " INTEGER NOT NULL ," +
            "UNIQUE(" + NewsAccessor.ID + "));";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public static String getDropTable() {
        return DROP_TABLE;
    }
}
