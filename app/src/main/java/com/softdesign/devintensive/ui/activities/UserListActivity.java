package com.softdesign.devintensive.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UserAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity {
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UserAdapter mUserAdapter;
    private List<UserListRes.UserData> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        loadUsers();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Показываем сообщение в нижней полоске
     *
     * @param message
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsers() {
        Call<UserListRes> call = mDataManager.getUserList();
        call.enqueue(new Callback<UserListRes>() {
                         @Override
                         public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                             switch (response.code()) {
                                 case 200:
                                     mUsers = response.body().getData();
                                     mUserAdapter = new UserAdapter(mUsers, new UserAdapter.UserViewHolder.CustomClickListener() {
                                         @Override
                                         public void onUserClikListener(int position) {

                                             UserDTO user  = new UserDTO(mUsers.get(position));
                                             Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                             profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, user);

                                             startActivity(profileIntent);

                                         }
                                     });
                                     mRecyclerView.setAdapter(mUserAdapter);
                                     break;
                                 case 404:
                                     showSnackbar(getString(R.string.auth_error));
                                     break;
                                 default:
                                     showSnackbar(getString(R.string.unexpected_error) + response.code());
                             }
                         }

                         @Override
                         public void onFailure(Call<UserListRes> call, Throwable t) {
                             //TODO Обработать ошибки
                         }
                     }

        );
    }

    /**
     * Настройка параметров тулбара
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настройка бокового меню
     */
    private void setupDrawer() {
        //TODO Реализовать переход в другую активити
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);

                if (item.getItemId() == R.id.user_profile_auth) { //Запуск активити логина
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivityForResult(intent, ConstantManager.REQUEST_AUTH);
                    return false;
                }

                showSnackbar(item.getTitle().toString());
                return false;
            }
        });
    }


}
