package com.softdesign.devintensive.ui.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private Button mSignIn;
    private EditText mUserLogin;
    private EditText mUserPassword;
    private TextView mRememberPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;


    /**
     * Инициализация внутренних переменных активити
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSignIn = (Button) findViewById(R.id.login_btn);
        mUserLogin = (EditText) findViewById(R.id.login_et);
        mUserPassword = (EditText) findViewById(R.id.password_et);
        mCoordinatorLayout = (CoordinatorLayout) findViewById((R.id.main_coordinator_container));
        mRememberPassword = (TextView) findViewById(R.id.forgot_pass_btn);
        mSignIn.setOnClickListener(this);
        mRememberPassword.setOnClickListener(this);

        mDataManager = DataManager.getInstance();

    }

    /**
     * Обработчик нажатий на различные объекты
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                signIn();
                break;
            case R.id.forgot_pass_btn:
                setRememberPassword();
                break;
        }
    }

    /**
     * Обработчик нажатия на кнопку "Назад"
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * Показываем сообщение в нижней полоске
     *
     * @param message
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setRememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userInfo) {

        showSnackbar("Добро пожаловать");
        mDataManager.getPreferencesManager().saveAuthToken(userInfo.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userInfo.getData().getUser().getId());

        saveUserValues(userInfo);
        saveUserInfo(userInfo);
        saveDrawerInfo(userInfo);
        saveUserPhotos(userInfo);

        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mUserLogin.getText().toString(), mUserPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    switch (response.code()) {
                        case 200:
                            loginSuccess(response.body());
                            break;
                        case 404:
                            showSnackbar(getString(R.string.auth_error));
                            break;
                        default:
                            showSnackbar(getString(R.string.unexpected_error) + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 11.07.2016 Обработать ошибки
                }
            });
        } else {
            showSnackbar("Нет подключения к интернету! Попробуйте позже!");
        }
    }

    private void saveUserValues(UserModelRes userInfo) {
        int[] userValues = {
                userInfo.getData().getUser().getProfileValues().getRaiting(),
                userInfo.getData().getUser().getProfileValues().getLinesCode(),
                userInfo.getData().getUser().getProfileValues().getProjects()
        };
        mDataManager.getPreferencesManager().saveUserInfoValues(userValues);


    }

    private void saveUserInfo(UserModelRes userInfo) {
        UserModelRes.User user = userInfo.getData().getUser();
        List<String> userInfoValues = new ArrayList<>();
        userInfoValues.add(user.getContacts().getPhone());
        userInfoValues.add(user.getContacts().getEmail());
        userInfoValues.add(user.getContacts().getVk());
        userInfoValues.add((user.getRepositories().getRepo().isEmpty() ? "" : user.getRepositories().getRepo().get(0).getGit()));
        userInfoValues.add(user.getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userInfoValues);

    }

    private void saveDrawerInfo(UserModelRes userInfo) {
        UserModelRes.User user = userInfo.getData().getUser();
        List<String> userInfoValues = new ArrayList<>();
        userInfoValues.add(user.getFirstName() + " " + user.getSecondName());
        userInfoValues.add(mUserLogin.getText().toString());

        mDataManager.getPreferencesManager().saveDrawerInfo(userInfoValues);

    }

    private void saveUserPhotos(UserModelRes userInfo) {

        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = getExternalCacheDir();
        } else {
            dir = getCacheDir();
        }

        try {
            final File avatar = File.createTempFile("avatar_uid_" + userInfo.getData().getUser().getId(), ".jpg", dir);
            final File photo = File.createTempFile("photo_uid_" + userInfo.getData().getUser().getId(), ".jpg", dir);

            mDataManager.getPreferencesManager().saveUserPhoto(Uri.fromFile(photo));
            mDataManager.getPreferencesManager().saveUserAvatar(Uri.fromFile(avatar));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(userInfo.getData().getUser().getPublicInfo().getPhoto())
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    } else {
                        FileOutputStream writer = new FileOutputStream(photo);
                        writer.write(response.body().bytes());
                        writer.close();
                    }

                }
            });

            request = new Request.Builder()
                    .url(userInfo.getData().getUser().getPublicInfo().getAvatar())
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    } else {
                        FileOutputStream writer = new FileOutputStream(avatar);
                        writer.write(response.body().bytes());
                        writer.close();
                    }

                }
            });
            }catch(IOException e){
                e.printStackTrace();
            }


        }
    }
