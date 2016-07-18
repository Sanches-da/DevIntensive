package com.softdesign.devintensive.utils;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.softdesign.devintensive.ui.activities.MainActivity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class DevintensiveApplication extends Application{

    private static SharedPreferences mSharedPreferences;
    private static Context mContext;

    @Override
    public void onCreate() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mContext = this;
        mContext = getApplicationContext();

        super.onCreate();
    }

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public static Context getContext() {
        return mContext;
    }

}
