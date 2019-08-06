package com.demoapp.inshort.application;

import android.app.Application;
import android.content.Context;

import com.demoapp.inshort.datastore.PrimaryDBProvider;
import com.demoapp.inshort.datastore.StandardStorageHelper;
import com.demoapp.inshort.view.toast.ToastManager;


/**
 * Created by aakashsingh on 16/09/17.
 */

public class InshortsApplication extends Application {

    private static Context mContext;
    private static InshortsApplication inshortsApplication;

    public InshortsApplication() {
    }

    private InshortsApplication(Context mContext) {
        InshortsApplication.mContext = mContext;
    }

    public static void createInstance(Context mContext) {
        InshortsApplication.mContext = mContext;
        inshortsApplication = new InshortsApplication(mContext);
    }

    public static Context getFintAppContext() {
        return mContext;
    }

    public static InshortsApplication getInstance() {
        return inshortsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            //create toast instance
            ToastManager.createInstance(this);
            //call database class instance
            PrimaryDBProvider.createInstance(this);
            StandardStorageHelper.createInstance(this);
            PrimaryDBProvider.getInstance().getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
