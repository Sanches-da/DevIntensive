package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import retrofit2.Call;

public class DataManager {
    private static DataManager sInstance = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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

    public Context getContext(){
        return mContext;
    }

    //region ====================== network
    public Call<UserModelRes> loginUser(UserLoginReq req){
        return mRestService.loginUser(req);
    }

    public Call<UserListRes> getUserList(){
        return mRestService.getUserList();
    }

    //endregion

    //========= DataBase ==============

}
