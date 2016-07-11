package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_ABOUT_KEY};

    public PreferencesManager() {
        mSharedPreferences = DevintensiveApplication.getmSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i=0; i<USER_FIELDS.length; i++){
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> res = new ArrayList<>();
        for (int i=0; i<USER_FIELDS.length; i++){
            res.add(mSharedPreferences.getString(USER_FIELDS[i],""));
        }

        return res;
    }

    public String loadUserProfileData(int index) {
        if (index>=0 && index<USER_FIELDS.length)
            return mSharedPreferences.getString(USER_FIELDS[index],"");
        else
            return "";
    }

    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        String res = mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/header_bg");

        return Uri.parse(res);
    }


}
