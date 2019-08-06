package com.demoapp.inshort.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.demoapp.inshort.Logger.Logger;
import com.demoapp.inshort.datastore.schema.MasterSQLiteSchemaBuilder;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class PrimaryDBProvider extends SQLiteOpenHelper {
    private final String TAG = PrimaryDBProvider.class.getSimpleName();
    private static final String DATABASE_NAME = "InshortDemoDatabase";
    private static final int DATABASE_VERSION = 4;
    private static PrimaryDBProvider primaryDBProvider;

    private PrimaryDBProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void createInstance(Context mContext) {
        if (primaryDBProvider == null) {
            primaryDBProvider = new PrimaryDBProvider(mContext);
        } else {
            throw new IllegalStateException("primaryDBProvider is already present");
        }
    }

    public static PrimaryDBProvider getInstance() {
        return primaryDBProvider;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            for (String sql : new MasterSQLiteSchemaBuilder().getPrimaryDBCreateTableQueries()) {
                db.execSQL(sql);
                System.out.println("INSIDE PrimaryDBProvider");
                Logger.putInDebugLog(TAG, sql, " executed...!!!");
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= newVersion) {
            try {
                db.beginTransaction();

                for (String sql : new MasterSQLiteSchemaBuilder().getPrimaryDBDropTableQueries()) {
                    db.execSQL(sql);
                    System.out.println("INSIDE PrimaryDBProvider");
                    Logger.putInDebugLog(TAG, sql, " executed...!!!");
                }

                for (String sql : new MasterSQLiteSchemaBuilder().getUpdateDBQueries(oldVersion, newVersion)) {
                    db.execSQL(sql);
                    System.out.println("INSIDE PrimaryDBProvider");
                    Logger.putInDebugLog(TAG, sql, " executed...!!!");
                }

                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }
}
