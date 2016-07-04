package com.softdesign.devintensive.data.managers;

public class DataManager {
    private static DataManager sInstance = null;
    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    public PreferencesManager getPreferencesManager() {
        return this.mPreferencesManager;
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }
}
