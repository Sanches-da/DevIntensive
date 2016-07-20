package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ChronosLoadUsers extends ChronosOperation<List<User>> {

    private DataManager mDataManager;

    private void saveUserInDb() {

        RepositoryDao mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();
        UserDao mUserDao = mDataManager.getDaoSession().getUserDao();

        Call<UserListRes> call = mDataManager.getUserListFromNetwork();

        try {
            Response<UserListRes> response = call.execute();
            if (response.code() == 200) {
                List<Repository> allRepositories = new ArrayList<Repository>();
                List<User> allUsers = new ArrayList<User>();

                for (UserListRes.UserData userRes : response.body().getData()) {
                    allRepositories.addAll(getRepoListFromUserData(userRes));
                    allUsers.add(new User(userRes));

                }
                mRepositoryDao.insertOrReplaceInTx(allRepositories);
                mUserDao.insertOrReplaceInTx(allUsers);
            } else {
                throw new Exception(DevintensiveApplication.getContext().getResources().getString(R.string.unexpected_error));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private List<Repository> getRepoListFromUserData(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repos = new ArrayList<>();
        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repos.add(new Repository(repo, userId));
        }
        return repos;
    }


    @Nullable
    @Override
    public List<User> run() {

        mDataManager = DataManager.getInstance();

        saveUserInDb();

        List<User> mUsers = mDataManager.getUserListFromDb();

        return mUsers;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<User>> {
    }
}
