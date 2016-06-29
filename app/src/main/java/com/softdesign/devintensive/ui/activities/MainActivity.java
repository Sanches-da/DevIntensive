package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX+"Main Activity";
    private DataManager mDataManager;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFloatingActionButton;
    private List<EditText> mUserInfo;
    private boolean mEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setupToolbar();

        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);
        setupDrawer();

        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);

        EditText userPhone = (EditText) findViewById(R.id.phone_et);
        EditText userMail = (EditText) findViewById(R.id.email_et);
        EditText userVK = (EditText) findViewById(R.id.vk_et);
        EditText userGit = (EditText) findViewById(R.id.git_1_et);
        EditText userAbout = (EditText) findViewById(R.id.about_et);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(userPhone);
        mUserInfo.add(userMail);
        mUserInfo.add(userVK);
        mUserInfo.add(userGit);
        mUserInfo.add(userAbout);

        mDataManager = DataManager.getInstance();

        loadUserInfoValue();

        if (savedInstanceState == null){
            //Впервые;
        }else{
            mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
        }
        changeEditMode(mEditMode);

        Log.d(TAG, "onCreate");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (mNavigationDrawer != null) {
                mNavigationDrawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mEditMode){
            saveUserInfoValue();
        }
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer(){
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mEditMode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                changeEditMode(!mEditMode);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (//android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR &&
                keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (mNavigationDrawer != null)
                if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)){
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void changeEditMode(boolean mode){
        mEditMode = mode;
        for (EditText uv : mUserInfo){
            uv.setEnabled(mode);
            uv.setFocusable(mode);
            uv.setFocusableInTouchMode(mode);
        }

        if (mEditMode)
            mFloatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
        else {
            mFloatingActionButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            saveUserInfoValue();
        }

    }
    private void loadUserInfoValue(){
        List<String> data = mDataManager.getPreferencesManager().loadUserProfileData();
        for(int i =0; i<data.size(); i++ ){
            mUserInfo.get(i).setText(data.get(i));
        }

    }

    private void saveUserInfoValue(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < mUserInfo.size(); i++) {
            data.add(mUserInfo.get(i).getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(data);
    }

}
