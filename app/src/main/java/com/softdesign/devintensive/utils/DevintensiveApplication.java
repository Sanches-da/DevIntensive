package com.softdesign.devintensive.utils;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevintensiveApplication extends Application{

    private static SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate();
    }

    public static SharedPreferences getmSharedPreferences() {
        return mSharedPreferences;
    }



}
