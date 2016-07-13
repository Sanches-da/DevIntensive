package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_ABOUT_KEY
    };
    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_VALUE,
            ConstantManager.USER_PROJECTS_VALUE
    };
    private static final String[] DRAWER_VALUES = {
            ConstantManager.USER_NAME_KEY,
            ConstantManager.USER_LOGIN_KEY
    };


    public PreferencesManager() {
        mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            res.add(mSharedPreferences.getString(USER_FIELDS[i], ""));
        }

        return res;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }


    public void saveUserAvatar(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        String res = mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/header_bg");

        return Uri.parse(res);
    }

    public Uri loadUserAvatar() {
        String res = mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, "android.resource://com.softdesign.devintensive/drawable/avatar");

        return Uri.parse(res);
    }


    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();

    }

    public String getAuthToken() {
        String res = mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "");
        return res;
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();

    }

    public String getUserId() {
        String res = mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "");
        return res;
    }

    public void saveUserInfoValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();

    }

    public List<String> getUserInfoValues(){
        List<String> res = new ArrayList<>();
        for (int i = 0; i < USER_VALUES.length; i++) {
            res.add(mSharedPreferences.getString(USER_VALUES[i], "0"));
        }

        return res;
    }

    public void saveDrawerInfo(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < DRAWER_VALUES.length; i++) {
            editor.putString(DRAWER_VALUES[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> getDrawerInfo(){
        List<String> res = new ArrayList<>();
        for (int i = 0; i < DRAWER_VALUES.length; i++) {
            res.add(mSharedPreferences.getString(DRAWER_VALUES[i], "0"));
        }

        return res;
    }
}
