package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.data.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DataManager {
    private static DataManager sInstance = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    private Picasso mPicasso;

    private DaoSession mDaoSession;

    private Bus mBus;

    private ChronosConnector mConnector;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();
        this.mBus = new Bus();
        this.mConnector = new ChronosConnector();
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

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region ====================== network
    public Call<UserModelRes> loginUser(UserLoginReq req) {
        return mRestService.loginUser(req);
    }

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }

    //endregion

    //region ========= DataBase ==============
    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    //endregion


    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public List<User> getUserListByName(String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userList;
    }

    public Bus getBus() {
        return mBus;
    }

    public ChronosConnector getConnector() {
        return mConnector;
    }
}
